package com.example.s8131464assignment.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.s8131464assignment.data.Repository
import com.example.s8131464assignment.data.Result
import com.example.s8131464assignment.datastore.KeypassStore
import com.example.s8131464assignment.network.models.AuthResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: Repository
    private lateinit var keypassStore: KeypassStore
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        keypassStore = mockk(relaxed = true)
        viewModel = LoginViewModel(repository, keypassStore)
    }

    @Test
    fun `login should update uiState to loading when called`() = runTest {
        // Given
        val location = "footscray"
        val username = "testuser"
        val password = "testpass"
        val authResponse = AuthResponse(status = "success", keypass = "test-keypass")
        val successResult = Result.Success(authResponse)
        
        coEvery { repository.login(location, username, password) } returns successResult

        // When
        viewModel.login(location, username, password)
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNull(uiState.errorMessage)
        assertEquals("test-keypass", uiState.keypass)
    }

    @Test
    fun `login should update uiState with error when login fails`() = runTest {
        // Given
        val location = "footscray"
        val username = "testuser"
        val password = "wrongpass"
        val error = Exception("Invalid credentials")
        val errorResult = Result.Error(error)
        
        coEvery { repository.login(location, username, password) } returns errorResult

        // When
        viewModel.login(location, username, password)
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals("Login failed. Please try again.", uiState.errorMessage)
        assertNull(uiState.keypass)
    }

    @Test
    fun `login should handle null keypass in response`() = runTest {
        // Given
        val location = "footscray"
        val username = "testuser"
        val password = "testpass"
        val authResponse = AuthResponse(status = "success", keypass = null)
        val successResult = Result.Success(authResponse)
        
        coEvery { repository.login(location, username, password) } returns successResult

        // When
        viewModel.login(location, username, password)
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals("Invalid credentials. Please check your username and password.", uiState.errorMessage)
        assertNull(uiState.keypass)
    }

    @Test
    fun `login should handle blank keypass in response`() = runTest {
        // Given
        val location = "footscray"
        val username = "testuser"
        val password = "testpass"
        val authResponse = AuthResponse(status = "success", keypass = "")
        val successResult = Result.Success(authResponse)
        
        coEvery { repository.login(location, username, password) } returns successResult

        // When
        viewModel.login(location, username, password)
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals("Invalid credentials. Please check your username and password.", uiState.errorMessage)
        assertNull(uiState.keypass)
    }
}
