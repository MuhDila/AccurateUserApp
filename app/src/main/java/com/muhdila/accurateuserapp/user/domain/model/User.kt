package com.muhdila.accurateuserapp.user.domain.model

enum class Gender(val value: Int) {
    MALE(0),
    FEMALE(1);

    companion object {
        fun fromValue(value: Int): Gender =
            entries.firstOrNull { it.value == value } ?: MALE
    }
}

data class User(
    val id: String = "",
    val name: String,
    val address: String,
    val email: String,
    val phoneNumber: String,
    val city: String,
    val gender: Gender,
    val isSynced: Boolean = true
)
