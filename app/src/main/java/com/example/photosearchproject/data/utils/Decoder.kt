package com.example.photosearchproject.data.utils

import retrofit2.Response

fun <T> Response<T>.decode(): Result<T> {
    return if (isSuccessful) {
        body()?.let { Result.success(it) } ?: Result.failure(Exception("Empty response"))
    } else {
        Result.failure(Exception("API Error: ${code()}, message: ${message()}"))
    }
}