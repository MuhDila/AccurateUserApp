package com.muhdila.accurateuserapp.core.domain

sealed interface DataError : Error {
    sealed interface Remote : DataError {
        data class BadRequest(val message: String? = null) : Remote
        object Unauthorized : Remote
        object Forbidden : Remote
        object NotFound : Remote
        object RequestTimeout : Remote
        object TooManyRequests : Remote
        object NoInternet : Remote
        object Server : Remote
        object Serialization : Remote
        object Unknown : Remote
    }

    enum class Local : DataError {
        DISK_FULL,
        UNKNOWN
    }
}
