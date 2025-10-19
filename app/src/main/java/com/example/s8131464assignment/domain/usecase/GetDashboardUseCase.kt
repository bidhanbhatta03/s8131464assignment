package com.example.s8131464assignment.domain.usecase

import com.example.s8131464assignment.data.Repository
import com.example.s8131464assignment.data.Result
import com.example.s8131464assignment.datastore.KeypassStore
import com.example.s8131464assignment.network.models.DashboardItem
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case for handling dashboard data retrieval
 * Follows clean architecture principles by separating business logic from UI
 */
class GetDashboardUseCase @Inject constructor(
    private val repository: Repository,
    private val keypassStore: KeypassStore
) {
    suspend operator fun invoke(): Result<List<DashboardItem>> {
        return try {
            val keypass: String? = keypassStore.keypassFlow.first()
            if (keypass.isNullOrBlank()) {
                Result.Error(Exception("No valid keypass found"))
            } else {
                repository.getDashboard(keypass)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
