package com.example.s8131464assignment.data

import com.example.s8131464assignment.network.RetrofitClient
import com.example.s8131464assignment.network.ApiService
import com.example.s8131464assignment.network.models.AuthRequest
import com.example.s8131464assignment.network.models.AuthResponse
import com.example.s8131464assignment.network.models.DashboardItem
import com.example.s8131464assignment.network.models.DashboardResponse
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var repository: Repository

    @Before
    fun setup() {
        apiService = mockk()
        mockkObject(RetrofitClient)
        every { RetrofitClient.apiService } returns apiService
        repository = Repository()
    }

    @Test
    fun `login should return success when api call succeeds`() = runTest {
        // Given
        val location = "footscray"
        val username = "testuser"
        val password = "testpass"
        val authResponse = AuthResponse(status = "success", keypass = "test-keypass")
        
        coEvery { apiService.login(location, AuthRequest(username, password)) } returns authResponse

        // When
        val result = repository.login(location, username, password)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(authResponse, (result as Result.Success).data)
    }

    @Test
    fun `login should return error when api call throws exception`() = runTest {
        // Given
        val location = "footscray"
        val username = "testuser"
        val password = "testpass"
        val exception = Exception("Network error")
        
        coEvery { apiService.login(location, AuthRequest(username, password)) } throws exception

        // When
        val result = repository.login(location, username, password)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).throwable)
    }

    @Test
    fun `getDashboard should return success when api call succeeds`() = runTest {
        // Given
        val keypass = "test-keypass"
        val dashboardItems = listOf(
            DashboardItem(
                id = "1",
                property1 = "Test Item",
                property2 = "Test Property",
                description = "Test Description"
            )
        )
        val dashboardResponse = DashboardResponse(entities = dashboardItems, entityTotal = 1)
        
        coEvery { apiService.getDashboard(keypass) } returns dashboardResponse

        // When
        val result = repository.getDashboard(keypass)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(dashboardItems, (result as Result.Success).data)
    }

    @Test
    fun `getDashboard should return error when api call throws exception`() = runTest {
        // Given
        val keypass = "test-keypass"
        val exception = Exception("Network error")
        
        coEvery { apiService.getDashboard(keypass) } throws exception

        // When
        val result = repository.getDashboard(keypass)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).throwable)
    }
}
