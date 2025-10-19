package com.example.s8131464assignment.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.s8131464assignment.data.Repository
import com.example.s8131464assignment.data.Result
import com.example.s8131464assignment.datastore.KeypassStore
import com.example.s8131464assignment.network.models.DashboardItem
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
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
class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: Repository
    private lateinit var keypassStore: KeypassStore
    private lateinit var viewModel: DashboardViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        keypassStore = mockk()
        viewModel = DashboardViewModel(repository, keypassStore)
    }

    @Test
    fun `load should update uiState with dashboard data when successful`() = runTest {
        // Given
        val dashboardItems = listOf(
            DashboardItem(
                id = "1",
                property1 = "Test Item",
                property2 = "Test Property",
                description = "Test Description"
            )
        )
        val successResult = Result.Success(dashboardItems)
        
        coEvery { repository.getDashboard("test-keypass") } returns successResult

        // When
        viewModel.load("test-keypass")
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNull(uiState.errorMessage)
        assertEquals(dashboardItems, uiState.items)
    }

    @Test
    fun `load should show empty list when dashboard is empty`() = runTest {
        // Given
        val emptyResult = Result.Success<List<DashboardItem>>(emptyList())
        
        coEvery { repository.getDashboard("test-keypass") } returns emptyResult

        // When
        viewModel.load("test-keypass")
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNull(uiState.errorMessage)
        assertTrue(uiState.items.isEmpty())
    }

    @Test
    fun `load should show error message when repository fails`() = runTest {
        // Given
        val error = Exception("Network error")
        val errorResult = Result.Error(error)
        
        coEvery { repository.getDashboard("test-keypass") } returns errorResult

        // When
        viewModel.load("test-keypass")
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals("Failed to load dashboard. Please try again.", uiState.errorMessage)
        assertTrue(uiState.items.isEmpty())
    }

    @Test
    fun `load should set loading state initially`() = runTest {
        // Given
        val dashboardItems = listOf(
            DashboardItem(
                id = "1",
                property1 = "Test Item",
                property2 = "Test Property",
                description = "Test Description"
            )
        )
        val successResult = Result.Success(dashboardItems)
        
        coEvery { repository.getDashboard("test-keypass") } returns successResult

        // When
        viewModel.load("test-keypass")

        // Then - Check initial loading state
        val initialUiState = viewModel.uiState.value
        assertTrue(initialUiState.isLoading)
    }
}
