package com.example.s8131464assignment.domain.usecase

import com.example.s8131464assignment.data.Repository
import com.example.s8131464assignment.data.Result
import com.example.s8131464assignment.datastore.KeypassStore
import com.example.s8131464assignment.network.models.AuthResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    private lateinit var repository: Repository
    private lateinit var keypassStore: KeypassStore
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        repository = mockk()
        keypassStore = mockk(relaxed = true)
        loginUseCase = LoginUseCase(repository, keypassStore)
    }

    @Test
    fun `invoke should return success and save keypass when login is successful`() = runTest {
        // Given
        val location = "footscray"
        val username = "testuser"
        val password = "testpass"
        val keypass = "test-keypass"
        val authResponse = AuthResponse(status = "success", keypass = keypass)
        val successResult = Result.Success(authResponse)
        
        coEvery { repository.login(location, username, password) } returns successResult

        // When
        val result = loginUseCase(location, username, password)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(keypass, (result as Result.Success).data.keypass)
        coVerify { keypassStore.saveKeypass(keypass) }
    }

    @Test
    fun `invoke should return error when login fails`() = runTest {
        // Given
        val location = "footscray"
        val username = "testuser"
        val password = "wrongpass"
        val error = Exception("Invalid credentials")
        val errorResult = Result.Error(error)
        
        coEvery { repository.login(location, username, password) } returns errorResult

        // When
        val result = loginUseCase(location, username, password)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).throwable)
        coVerify(exactly = 0) { keypassStore.saveKeypass(any<String>()) }
    }

    @Test
    fun `invoke should not save keypass when response has null keypass`() = runTest {
        // Given
        val location = "footscray"
        val username = "testuser"
        val password = "testpass"
        val authResponse = AuthResponse(status = "success", keypass = null)
        val successResult = Result.Success(authResponse)
        
        coEvery { repository.login(location, username, password) } returns successResult

        // When
        val result = loginUseCase(location, username, password)

        // Then
        assertTrue(result is Result.Success)
        coVerify(exactly = 0) { keypassStore.saveKeypass(any<String>()) }
    }
}
