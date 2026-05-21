package com.muhdila.accurateuserapp.user.presentation.state

import com.muhdila.accurateuserapp.R
import com.muhdila.accurateuserapp.core.presentation.UiText
import com.muhdila.accurateuserapp.user.domain.model.Gender

data class UserFormState(
    val name: String = "",
    val nameError: UiText? = null,

    val address: String = "",

    val email: String = "",
    val emailError: UiText? = null,

    val phoneNumber: String = "",
    val phoneError: UiText? = null,

    val city: String = "",
    val cityError: UiText? = null,

    val gender: Gender = Gender.MALE,

    val canSubmit: Boolean = false
)

fun UserFormState.validate(): UserFormState {
    val nameError: UiText? = when {
        name.isBlank() -> UiText.StringResourceId(R.string.error_name_empty)
        name.length < 2 -> UiText.StringResourceId(R.string.error_name_too_short)
        else -> null
    }
    val emailError: UiText? = when {
        email.isBlank() -> UiText.StringResourceId(R.string.error_email_empty)
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
            UiText.StringResourceId(R.string.error_email_invalid)
        else -> null
    }
    val phoneError: UiText? = when {
        phoneNumber.isBlank() -> UiText.StringResourceId(R.string.error_phone_empty)
        phoneNumber.length < 6 -> UiText.StringResourceId(R.string.error_phone_too_short)
        else -> null
    }
    val cityError: UiText? = when {
        city.isBlank() -> UiText.StringResourceId(R.string.error_city_empty)
        else -> null
    }
    return copy(
        nameError = nameError,
        emailError = emailError,
        phoneError = phoneError,
        cityError = cityError,
        canSubmit = listOf(nameError, emailError, phoneError, cityError).all { it == null }
            && name.isNotBlank() && email.isNotBlank()
            && phoneNumber.isNotBlank() && city.isNotBlank()
    )
}
