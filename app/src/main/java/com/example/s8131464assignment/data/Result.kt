package com.example.s8131464assignment.data

/**
 * A sealed class representing the result of an operation that can either succeed or fail.
 * 
 * This pattern is used throughout the app to handle asynchronous operations, particularly
 * network requests, in a type-safe manner without throwing exceptions.
 * 
 * @param T The type of data returned in case of success
 * 
 * @see Success for successful results containing data
 * @see Error for error results containing exception information
 */
sealed class Result<out T> {
    /**
     * Represents a successful result containing the requested data.
     * 
     * @property data The successfully retrieved data of type [T]
     */
    data class Success<T>(val data: T) : Result<T>()
    
    /**
     * Represents an error result containing exception information.
     * 
     * @property throwable The exception that caused the operation to fail
     */
    data class Error(val throwable: Throwable) : Result<Nothing>()
}


