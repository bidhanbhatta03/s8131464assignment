package com.example.s8131464assignment.data

import com.example.s8131464assignment.network.RetrofitClient
import com.example.s8131464assignment.network.models.AuthRequest
import com.example.s8131464assignment.network.models.AuthResponse
import com.example.s8131464assignment.network.models.DashboardItem

class Repository {
    suspend fun login(location: String, username: String, password: String): Result<AuthResponse> {
        return try {
            val res = RetrofitClient.apiService.login(location, AuthRequest(username, password))
            Result.Success(res)
        } catch (t: Throwable) {
            Result.Error(t)
        }
    }

    suspend fun getDashboard(keypass: String): Result<List<DashboardItem>> {
        return try {
            val res = RetrofitClient.apiService.getDashboard(keypass)
            Result.Success(res.entities)
        } catch (t: Throwable) {
            Result.Error(t)
        }
    }
}


