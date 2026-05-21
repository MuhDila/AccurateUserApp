package com.muhdila.accurateuserapp.core.domain

sealed interface Result<out D, out E> {
    data class Success<out D>(val data: D): Result<D, Nothing>
    data class Error<out E : com.muhdila.accurateuserapp.core.domain.Error>(val error: E): Result<Nothing, E>
}
