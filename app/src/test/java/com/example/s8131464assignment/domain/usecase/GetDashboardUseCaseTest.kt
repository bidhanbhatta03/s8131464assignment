package com.example.s8131464assignment.domain.usecase

import com.example.s8131464assignment.data.Repository
import com.example.s8131464assignment.data.Result
import com.example.s8131464assignment.datastore.KeypassStore
import com.example.s8131464assignment.network.models.DashboardItem
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetDashboardUseCaseTest {

    private lateinit var repository: Repository
    private lateinit var keypassStore: KeypassStore
    private lateinit var getDashboardUseCase: GetDashboardUseCase

    @Before
    fun setup() {
        repository = mockk()
        keypassStore = mockk()
        getDashboardUseCase = GetDashboardUseCase(repository, keypassStore)
    }

    @Test
    fun `invoke should return success when keypass exists and dashboard data is retrieved`() = runTest {
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
        val successResult = Result.Success(dashboardItems)
        
        every { keypassStore.keypassFlow } returns flowOf(keypass)
        coEvery { repository.getDashboard(keypass) } returns successResult

        // When
        val result = getDashboardUseCase()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(dashboardItems, (result as Result.Success).data)
    }

    @Test
    fun `invoke should return error when keypass is null`() = runTest {
        // Given
        every { keypassStore.keypassFlow } returns flowOf(null)

        // When
        val result = getDashboardUseCase()

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).throwable.message?.contains("No valid keypass found") == true)
    }

    @Test
    fun `invoke should return error when keypass is blank`() = runTest {
        // Given
        every { keypassStore.keypassFlow } returns flowOf("")

        // When
        val result = getDashboardUseCase()

        // Then
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).throwable.message?.contains("No valid keypass found") == true)
    }

    @Test
    fun `invoke should return error when repository throws exception`() = runTest {
        // Given
        val keypass = "test-keypass"
        val exception = Exception("Network error")
        
        every { keypassStore.keypassFlow } returns flowOf(keypass)
        coEvery { repository.getDashboard(keypass) } returns Result.Error(exception)

        // When
        val result = getDashboardUseCase()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(exception, (result as Result.Error).throwable)
    }
}
