package com.example.comics.repository.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

sealed class NetworkResponse<out T> {
    data class Success<out T>(val item: T) : NetworkResponse<T>()
    data class Error(val exception: Exception) : NetworkResponse<Nothing>()
}

fun <T> NetworkResponse<T>.toFlow(): Flow<T> {
    val networkResponse = this

    return flow {
        when (networkResponse) {
            is NetworkResponse.Success -> {
                this.emit(networkResponse.item)
            }

           is NetworkResponse.Error -> {
               throw networkResponse.exception
           }
        }
    }
}