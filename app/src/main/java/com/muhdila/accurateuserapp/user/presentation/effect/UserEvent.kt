package com.muhdila.accurateuserapp.user.presentation.effect

import com.muhdila.accurateuserapp.user.domain.model.Gender
import com.muhdila.accurateuserapp.user.presentation.state.SortType

sealed interface UserEvent {
    
    data class SearchQueryChanged(val query: String) : UserEvent
    data class CityFilterSelected(val city: String?) : UserEvent
    data class SortTypeChanged(val sortType: SortType) : UserEvent
    object Refresh : UserEvent

    object ShowAddDialog : UserEvent
    object DismissAddDialog : UserEvent
    object SubmitAddUser : UserEvent

    data class NameChanged(val name: String) : UserEvent
    data class AddressChanged(val address: String) : UserEvent
    data class EmailChanged(val email: String) : UserEvent
    data class PhoneChanged(val phone: String) : UserEvent
    data class CityChanged(val city: String) : UserEvent
    data class GenderChanged(val gender: Gender) : UserEvent
}
