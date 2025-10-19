package com.example.s8131464assignment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8131464assignment.data.Repository
import com.example.s8131464assignment.data.Result
import com.example.s8131464assignment.datastore.KeypassStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val keypass: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val keypassStore: KeypassStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(location: String, username: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            when (val res = repository.login(location, username, password)) {
                is Result.Success -> {
                    val key = res.data.keypass
                    if (!key.isNullOrBlank()) {
                        keypassStore.saveKeypass(key)
                        _uiState.value = LoginUiState(isLoading = false, keypass = key)
                    } else {
                        _uiState.value = LoginUiState(isLoading = false, errorMessage = "Invalid credentials. Please check your username and password.")
                    }
                }
                is Result.Error -> {
                    val errorMsg = when {
                        res.throwable.message?.contains("404") == true -> 
                            "Invalid location or credentials. Please verify your selection and try again."
                        res.throwable.message?.contains("timeout", ignoreCase = true) == true || 
                        res.throwable.message?.contains("connect", ignoreCase = true) == true -> 
                            "Connection error. Please check your internet connection and try again."
                        res.throwable.message?.contains("401") == true || 
                        res.throwable.message?.contains("403") == true -> 
                            "Authentication failed. Invalid username or password."
                        res.throwable.message?.contains("500") == true || 
                        res.throwable.message?.contains("503") == true -> 
                            "Server error. Please try again later."
                        else -> res.throwable.localizedMessage ?: "Login failed. Please try again."
                    }
                    _uiState.value = LoginUiState(isLoading = false, errorMessage = errorMsg)
                }
            }
        }
    }
}


