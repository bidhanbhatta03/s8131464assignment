package com.example.s8131464assignment.di

import android.content.Context
import com.example.s8131464assignment.data.Repository
import com.example.s8131464assignment.datastore.KeypassStore
import com.example.s8131464assignment.network.ApiService
import com.example.s8131464assignment.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService = RetrofitClient.apiService

    @Provides
    @Singleton
    fun provideRepository(): Repository = Repository()

    @Provides
    @Singleton
    fun provideKeypassStore(@ApplicationContext context: Context): KeypassStore = KeypassStore(context)
}


