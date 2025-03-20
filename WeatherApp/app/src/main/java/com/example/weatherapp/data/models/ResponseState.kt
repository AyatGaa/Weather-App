package com.example.weatherapp.data.models

sealed class ResponseState<out T> {

    data object Loading : ResponseState<Nothing>()

    data class Success<T>(val data:  T) : ResponseState<T>()

    data class Failure(val error: Throwable) : ResponseState<Nothing>()
}