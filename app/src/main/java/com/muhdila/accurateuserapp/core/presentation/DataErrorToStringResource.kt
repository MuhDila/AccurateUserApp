package com.muhdila.accurateuserapp.core.presentation

import com.muhdila.accurateuserapp.R
import com.muhdila.accurateuserapp.core.domain.DataError

fun DataError.toUiText(): UiText {
    val stringRes = when(this) {
        DataError.Remote.UNAUTHORIZED -> R.string.error_unauthorized
        DataError.Remote.BAD_REQUEST -> R.string.error_bad_request
        DataError.Remote.FORBIDDEN -> R.string.error_forbidden
        DataError.Remote.NOT_FOUND -> R.string.error_not_found
        DataError.Local.DISK_FULL -> R.string.error_disk_full
        DataError.Local.UNKNOWN -> R.string.error_unknown
        DataError.Remote.REQUEST_TIMEOUT -> R.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUESTS -> R.string.error_too_many_requests
        DataError.Remote.NO_INTERNET -> R.string.error_no_internet
        DataError.Remote.SERVER -> R.string.error_unknown
        DataError.Remote.SERIALIZATION -> R.string.error_serialization
        DataError.Remote.UNKNOWN -> R.string.error_unknown
    }
    
    return UiText.StringResourceId(stringRes)
}
