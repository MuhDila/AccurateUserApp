package com.muhdila.accurateuserapp.user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhdila.accurateuserapp.R
import com.muhdila.accurateuserapp.core.domain.Result
import com.muhdila.accurateuserapp.core.presentation.UiText
import com.muhdila.accurateuserapp.core.presentation.toUiText
import com.muhdila.accurateuserapp.core.domain.analytics.IAnalyticsTracker
import com.muhdila.accurateuserapp.user.domain.model.User
import com.muhdila.accurateuserapp.user.domain.usecase.AddUserUseCase
import com.muhdila.accurateuserapp.user.domain.usecase.GetUsersUseCase
import com.muhdila.accurateuserapp.user.domain.usecase.SyncUsersUseCase
import com.muhdila.accurateuserapp.user.presentation.effect.UserEffect
import com.muhdila.accurateuserapp.user.presentation.effect.UserEvent
import com.muhdila.accurateuserapp.user.presentation.state.SortType
import com.muhdila.accurateuserapp.user.presentation.state.UserFormState
import com.muhdila.accurateuserapp.user.presentation.state.UserUiState
import com.muhdila.accurateuserapp.user.presentation.state.validate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val syncUsersUseCase: SyncUsersUseCase,
    private val analyticsTracker: IAnalyticsTracker
) : ViewModel() {

    private val _state = MutableStateFlow(UserUiState())
    val state: StateFlow<UserUiState> = _state.asStateFlow()

    private val _effect = Channel<UserEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        analyticsTracker.logEvent("user_screen_view")
        observeUsers()
        syncUsers()
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
                analyticsTracker.logEvent("user_search_query_changed", mapOf("query" to event.query))
                applyFilters()
            }
            is UserEvent.CityFilterSelected -> {
                _state.update { it.copy(selectedCity = event.city) }
                analyticsTracker.logEvent("user_city_filter_changed", mapOf("city" to (event.city ?: "all")))
                applyFilters()
            }
            is UserEvent.SortTypeChanged -> {
                _state.update { it.copy(sortType = event.sortType) }
                analyticsTracker.logEvent("user_sort_type_changed", mapOf("sort_type" to event.sortType.name))
                applyFilters()
            }
            is UserEvent.Refresh -> syncUsers()

            is UserEvent.ShowAddDialog ->
                _state.update { it.copy(showAddDialog = true, formState = UserFormState()) }
            is UserEvent.DismissAddDialog ->
                _state.update { it.copy(showAddDialog = false) }
            is UserEvent.SubmitAddUser -> submitAddUser()

            is UserEvent.NameChanged ->
                updateAndValidate { it.copy(name = event.name) }
            is UserEvent.AddressChanged ->
                updateAndValidate { it.copy(address = event.address) }
            is UserEvent.EmailChanged ->
                updateAndValidate { it.copy(email = event.email) }
            is UserEvent.PhoneChanged ->
                updateAndValidate { it.copy(phoneNumber = event.phone) }
            is UserEvent.CityChanged ->
                updateAndValidate { it.copy(city = event.city) }
            is UserEvent.GenderChanged ->
                updateAndValidate { it.copy(gender = event.gender) }
        }
    }

    private fun observeUsers() {
        viewModelScope.launch {
            getUsersUseCase().collect { users ->
                val cities = users.map { it.city }.filter { it.isNotBlank() }.distinct().sorted()
                _state.update { state ->
                    state.copy(
                        users = users,
                        availableCities = cities,
                        hasPendingSync = users.any { !it.isSynced },
                        isLoading = false
                    )
                }
                applyFilters()
            }
        }
    }

    private fun syncUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = syncUsersUseCase()) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(
                        UserEffect.ShowSnackbar(result.error.toUiText())
                    )
                }
            }
        }
    }

    private fun submitAddUser() {
        val form = _state.value.formState.validate()
        _state.update { it.copy(formState = form) }
        if (!form.canSubmit) return

        viewModelScope.launch {
            _state.update { it.copy(isAddingUser = true) }
            val user = User(
                name = form.name.trim(),
                address = form.address.trim(),
                email = form.email.trim(),
                phoneNumber = form.phoneNumber.trim(),
                city = form.city.trim(),
                gender = form.gender
            )
            when (val result = addUserUseCase(user)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isAddingUser = false,
                            showAddDialog = false,
                            newlyCreatedUserId = result.data.id
                        )
                    }
                    analyticsTracker.logEvent("user_added_locally", mapOf("name" to user.name, "city" to user.city))
                    
                    val messageRes = if (result.data.isSynced) {
                        R.string.user_added_successfully
                    } else {
                        R.string.user_added_offline_successfully
                    }
                    _effect.send(UserEffect.ShowSnackbar(UiText.StringResourceId(messageRes)))

                    viewModelScope.launch {
                        kotlinx.coroutines.delay(1500)
                        _state.update { it.copy(newlyCreatedUserId = null) }
                    }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isAddingUser = false,
                            showAddDialog = false
                        )
                    }
                    _effect.send(UserEffect.ShowSnackbar(result.error.toUiText()))
                }
            }
        }
    }

    private fun applyFilters() {
        val state = _state.value
        var result = state.users

        if (state.searchQuery.isNotBlank()) {
            val query = state.searchQuery.lowercase()
            result = result.filter {
                it.name.lowercase().contains(query) ||
                it.city.lowercase().contains(query) ||
                it.email.lowercase().contains(query)
            }
        }

        if (state.selectedCity != null) {
            result = result.filter { it.city == state.selectedCity }
        }

        result = when (state.sortType) {
            SortType.NAME_ASC  -> result.sortedBy { it.name.lowercase() }
            SortType.NAME_DESC -> result.sortedByDescending { it.name.lowercase() }
            SortType.NONE      -> result
        }

        if (state.newlyCreatedUserId != null) {
            val newlyCreatedUser = result.firstOrNull { it.id == state.newlyCreatedUserId }
            if (newlyCreatedUser != null) {
                result = listOf(newlyCreatedUser) + result.filter { it.id != state.newlyCreatedUserId }
            }
        }

        _state.update { it.copy(displayedUsers = result) }
    }

    private fun updateAndValidate(transform: (UserFormState) -> UserFormState) {
        _state.update { state ->
            val newForm = transform(state.formState).validate()
            state.copy(formState = newForm)
        }
    }
}
