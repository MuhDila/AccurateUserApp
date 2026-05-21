package com.muhdila.accurateuserapp.core.presentation

import com.muhdila.accurateuserapp.R
import com.muhdila.accurateuserapp.core.domain.DataError

fun DataError.toUiText(): UiText {
    return when(this) {
        is DataError.Remote.BadRequest -> {
            if (!this.message.isNullOrBlank()) {
                UiText.DynamicString(this.message)
            } else {
                UiText.StringResourceId(R.string.error_bad_request)
            }
        }
        is DataError.Remote.Unauthorized -> UiText.StringResourceId(R.string.error_unauthorized)
        is DataError.Remote.Forbidden -> UiText.StringResourceId(R.string.error_forbidden)
        is DataError.Remote.NotFound -> UiText.StringResourceId(R.string.error_not_found)
        DataError.Local.DISK_FULL -> UiText.StringResourceId(R.string.error_disk_full)
        DataError.Local.UNKNOWN -> UiText.StringResourceId(R.string.error_unknown)
        is DataError.Remote.RequestTimeout -> UiText.StringResourceId(R.string.error_request_timeout)
        is DataError.Remote.TooManyRequests -> UiText.StringResourceId(R.string.error_too_many_requests)
        is DataError.Remote.NoInternet -> UiText.StringResourceId(R.string.error_no_internet)
        is DataError.Remote.Server -> UiText.StringResourceId(R.string.error_unknown)
        is DataError.Remote.Serialization -> UiText.StringResourceId(R.string.error_serialization)
        is DataError.Remote.Unknown -> UiText.StringResourceId(R.string.error_unknown)
    }
}
