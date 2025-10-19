package com.example.s8131464assignment.domain.usecase

import com.example.s8131464assignment.data.Result
import com.example.s8131464assignment.data.Repository
import com.example.s8131464assignment.datastore.KeypassStore
import com.example.s8131464assignment.network.models.AuthResponse
import javax.inject.Inject

/**
 * Use case for handling login operations
 * Follows clean architecture principles by separating business logic from UI
 */
class LoginUseCase @Inject constructor(
    private val repository: Repository,
    private val keypassStore: KeypassStore
) {
    suspend operator fun invoke(
        location: String,
        username: String,
        password: String
    ): Result<AuthResponse> {
        return when (val result = repository.login(location, username, password)) {
            is Result.Success -> {
                // Save keypass if login successful
                result.data.keypass?.let { keypass ->
                    keypassStore.saveKeypass(keypass)
                }
                result
            }
            is Result.Error -> result
        }
    }
}
