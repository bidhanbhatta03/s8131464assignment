package com.example.s8131464assignment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8131464assignment.data.Repository
import com.example.s8131464assignment.data.Result
import com.example.s8131464assignment.datastore.KeypassStore
import com.example.s8131464assignment.network.models.DashboardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.first

data class DashboardUiState(
    val isLoading: Boolean = false,
    val items: List<DashboardItem> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: Repository,
    private val keypassStore: KeypassStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun load(keypass: String?) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val key = keypass ?: try { keypassStore.keypassFlow.first() } catch (e: Exception) { null }
            if (key.isNullOrBlank()) {
                _uiState.value = DashboardUiState(isLoading = false, items = emptyList(), errorMessage = "Missing keypass")
                return@launch
            }
            when (val res = repository.getDashboard(key)) {
                is Result.Success -> {
                    _uiState.value = DashboardUiState(isLoading = false, items = res.data ?: emptyList())
                }
                is Result.Error -> {
                    val errorMsg = when {
                        res.throwable.message?.contains("timeout", ignoreCase = true) == true || 
                        res.throwable.message?.contains("connect", ignoreCase = true) == true -> 
                            "Connection error. Unable to load data."
                        res.throwable.message?.contains("404") == true -> 
                            "Data not found. Please login again."
                        res.throwable.message?.contains("401") == true || 
                        res.throwable.message?.contains("403") == true -> 
                            "Session expired. Please login again."
                        else -> res.throwable.localizedMessage ?: "Failed to load dashboard. Please try again."
                    }
                    _uiState.value = DashboardUiState(isLoading = false, items = emptyList(), errorMessage = errorMsg)
                }
            }
        }
    }
}


