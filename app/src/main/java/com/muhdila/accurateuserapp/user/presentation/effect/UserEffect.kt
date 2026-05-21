package com.muhdila.accurateuserapp.user.presentation.effect

import com.muhdila.accurateuserapp.core.presentation.UiText

sealed interface UserEffect {
    data class ShowSnackbar(val message: UiText) : UserEffect
}
