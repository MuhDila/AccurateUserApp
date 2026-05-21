package com.muhdila.accurateuserapp.user.presentation.state

import com.muhdila.accurateuserapp.user.domain.model.User

data class UserUiState(
    val isLoading: Boolean = true,
    val users: List<User> = emptyList(),
    val displayedUsers: List<User> = emptyList(),
    val searchQuery: String = "",
    val selectedCity: String? = null,
    val availableCities: List<String> = emptyList(),
    val sortType: SortType = SortType.NONE,
    val hasPendingSync: Boolean = false,
    val showAddDialog: Boolean = false,
    val isAddingUser: Boolean = false,
    val formState: UserFormState = UserFormState(),
    val newlyCreatedUserId: String? = null
)
