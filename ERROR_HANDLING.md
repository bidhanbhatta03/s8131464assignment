# Error Handling Documentation

## Overview
This document outlines the error handling patterns and strategies used throughout the application.

## Architecture

### Result Pattern
The application uses a sealed `Result` class to handle success and error states:

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val throwable: Throwable) : Result<Nothing>()
}
```

### Error Flow
1. **Network Layer**: Errors originate from API calls in `ApiService`
2. **Repository Layer**: Catches exceptions and wraps them in `Result.Error`
3. **Use Case Layer**: Propagates or transforms errors with business logic
4. **ViewModel Layer**: Maps errors to user-friendly messages
5. **UI Layer**: Displays error messages to users

## Error Types

### Network Errors
- **Connection Errors**: Network unavailable, timeout
- **HTTP Errors**: 
  - 401/403: Authentication failures
  - 404: Resource not found
  - 500/503: Server errors

### Business Logic Errors
- **Missing Keypass**: When authentication token is not available
- **Invalid Credentials**: Username/password combination is incorrect
- **Session Expired**: When the authentication token is no longer valid

### Validation Errors
- **Empty Fields**: Required fields not filled
- **Invalid Format**: Data doesn't meet format requirements

## Error Handling Strategies

### Repository Layer
```kotlin
suspend fun login(location: String, username: String, password: String): Result<AuthResponse> {
    return try {
        val res = RetrofitClient.apiService.login(location, AuthRequest(username, password))
        Result.Success(res)
    } catch (t: Throwable) {
        Result.Error(t)  // Wrap all exceptions
    }
}
```

### Use Case Layer
```kotlin
suspend operator fun invoke(): Result<List<DashboardItem>> {
    return try {
        val keypass = keypassStore.keypassFlow.first()
        if (keypass.isNullOrBlank()) {
            Result.Error(Exception("No valid keypass found"))  // Business logic error
        } else {
            repository.getDashboard(keypass)
        }
    } catch (e: Exception) {
        Result.Error(e)
    }
}
```

### ViewModel Layer
Error messages are mapped based on error type:

```kotlin
when (res) {
    is Result.Error -> {
        val errorMsg = when {
            res.throwable.message?.contains("404") == true -> 
                "Invalid location or credentials"
            res.throwable.message?.contains("timeout") == true -> 
                "Connection error"
            res.throwable.message?.contains("401") == true -> 
                "Authentication failed"
            else -> res.throwable.localizedMessage ?: "Unknown error"
        }
        _uiState.value = LoginUiState(errorMessage = errorMsg)
    }
}
```

## Best Practices

### 1. Always Catch Exceptions
- Wrap all network calls in try-catch blocks
- Convert exceptions to Result.Error

### 2. Provide User-Friendly Messages
- Map technical errors to understandable messages
- Use string resources for localization

### 3. Log Errors for Debugging
```kotlin
catch (e: Exception) {
    Log.e(TAG, "Error occurred", e)
    Result.Error(e)
}
```

### 4. Handle Specific Error Cases
- Check HTTP status codes
- Provide specific actions for recoverable errors

### 5. Graceful Degradation
- Show cached data when network fails
- Provide retry mechanisms
- Maintain app stability

## Testing Error Scenarios

### Unit Tests
```kotlin
@Test
fun `should return error when api call fails`() = runTest {
    // Given
    val exception = Exception("Network error")
    coEvery { apiService.login(any(), any()) } throws exception
    
    // When
    val result = repository.login("location", "user", "pass")
    
    // Then
    assertTrue(result is Result.Error)
}
```

### Integration Tests
- Test network timeout scenarios
- Test invalid credentials
- Test server error responses

## Monitoring and Analytics

### Recommended Practices
1. **Track Error Rates**: Monitor frequency of different error types
2. **Log Stack Traces**: Capture full error context in production
3. **User Impact**: Track how errors affect user experience
4. **Recovery Success**: Monitor successful retries after errors

## Future Improvements

1. **Retry Logic**: Implement exponential backoff for network errors
2. **Offline Mode**: Cache data for offline access
3. **Error Recovery**: Automated recovery strategies
4. **Analytics Integration**: Send error metrics to analytics service
5. **Custom Error Types**: Create domain-specific error classes

## Quick Reference

| Error Type | User Message | Recovery Action |
|------------|--------------|-----------------|
| Network Timeout | "Connection error. Please check your internet." | Retry |
| 401 Unauthorized | "Session expired. Please login again." | Re-login |
| 404 Not Found | "Data not found." | Go back |
| 500 Server Error | "Server error. Please try again later." | Retry later |
| No Internet | "No internet connection." | Check connection |
| Invalid Input | "Please check your input." | Correct input |