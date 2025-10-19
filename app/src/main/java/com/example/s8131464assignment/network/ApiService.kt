package com.example.s8131464assignment.network

import com.example.s8131464assignment.network.models.AuthRequest
import com.example.s8131464assignment.network.models.AuthResponse
import com.example.s8131464assignment.network.models.DashboardItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("{location}/auth")
    suspend fun login(
        @Path("location") location: String,
        @Body body: AuthRequest
    ): AuthResponse

    @GET("dashboard/{keypass}")
    suspend fun getDashboard(
        @Path("keypass") keypass: String
    ): com.example.s8131464assignment.network.models.DashboardResponse
}


