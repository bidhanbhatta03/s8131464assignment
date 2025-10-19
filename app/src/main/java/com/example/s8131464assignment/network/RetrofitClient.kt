package com.example.s8131464assignment.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://nit3213api.onrender.com/"
    
    // Increased timeouts for cold start scenarios
    private const val CONNECT_TIMEOUT = 60L
    private const val READ_TIMEOUT = 60L
    private const val WRITE_TIMEOUT = 60L

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    
    // Retry interceptor for network resilience
    private val retryInterceptor = Interceptor { chain ->
        val request = chain.request()
        var response: Response? = null
        var exception: IOException? = null
        var tryCount = 0
        val maxRetries = 3
        
        while (tryCount < maxRetries && response?.isSuccessful != true) {
            try {
                response?.close() // Close previous response if exists
                response = chain.proceed(request)
                if (response.isSuccessful) {
                    return@Interceptor response
                }
            } catch (e: IOException) {
                exception = e
                tryCount++
                if (tryCount >= maxRetries) {
                    throw e
                }
                // Wait before retrying (exponential backoff)
                try {
                    Thread.sleep((Math.pow(2.0, tryCount.toDouble()) * 1000).toLong())
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    throw IOException("Retry interrupted", e)
                }
            }
        }
        
        response ?: throw (exception ?: IOException("Unknown error during retry"))
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(retryInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
}


