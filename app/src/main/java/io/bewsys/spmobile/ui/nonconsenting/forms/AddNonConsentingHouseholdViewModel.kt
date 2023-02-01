package io.bewsys.spmobile.ui.nonconsenting.forms

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.bewsys.spmobile.data.model.NonConsentHousehold
import io.bewsys.spmobile.data.repository.NonConsentingHouseholdRepositoryImpl
import io.bewsys.spmobile.ui.ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddNonConsentingHouseholdViewModel(
    private val state: SavedStateHandle,
    private val repository: NonConsentingHouseholdRepositoryImpl
) : ViewModel() {

    private val addNonConsentingHouseholdChannel = Channel<AddNonConsentingHouseholdEvent>()
    val addNonConsentingHouseholdEvent = addNonConsentingHouseholdChannel.receiveAsFlow()

    var reason = state.get<String>("reason") ?: ""
        set(value) {
            field = value
            state["reason"] = value
        }
    var otherReason = state.get<String>("otherReason") ?: ""
        set(value) {
            field = value
            state["otherReason"] = value
        }
    var province = state.get<String>("province") ?: ""
        set(value) {
            field = value
            state["province"] = value
        }
    var territory = state.get<String>("territory") ?: ""
        set(value) {
            field = value
            state["territory"] = value
        }
    var community = state.get<String>("community") ?: ""
        set(value) {
            field = value
            state["community"] = value
        }
    var groupment = state.get<String>("groupment") ?: ""
        set(value) {
            field = value
            state["groupment"] = value
        }
    var address = state.get<String>("address") ?: ""
        set(value) {
            field = value
            state["address"] = value
        }

    var lon = state.get<String>("lon") ?: ""
        set(value) {
            field = value
            state["lon"] = value
        }
    var lat = state.get<String>("lat") ?: ""
        set(value) {
            field = value
            state["lat"] = value
        }

    var province_id:Long? = null
    var community_id:Long? = null

    fun onRegisterClicked() {
        if (reason.isBlank() || otherReason.isBlank() || province.isBlank() || territory.isBlank() || community.isBlank() || groupment.isBlank() || address.isBlank() || lon.isBlank() || lat.isBlank()) {
            showInvalidInputMessage()
            return
        } else {
            NonConsentHousehold(
                province_id = province_id,
                community_id = community_id,
                gps_latitude = lat,
                gps_longitude = lon,
                reason = reason,
                other_non_consent_reason = otherReason
            ).also {
                addNonConsentingHousehold(it)
            }

        }
    }

    private fun addNonConsentingHousehold(newNonConsentingHousehold: NonConsentHousehold) =
        viewModelScope.launch {

//            repository.insertNonConsentingHousehold(newNonConsentingHousehold)

            addNonConsentingHouseholdChannel.send(
                AddNonConsentingHouseholdEvent.NavigateBackWithResults(
                    ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK
                )
            )

        }

    private fun showInvalidInputMessage() = viewModelScope.launch {
        addNonConsentingHouseholdChannel.send(
            AddNonConsentingHouseholdEvent.ShowInvalidInputMessage(
                "One or more text fields might be empty!"
            )
        )
    }

    sealed class AddNonConsentingHouseholdEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddNonConsentingHouseholdEvent()
        data class NavigateBackWithResults(val results: Int) : AddNonConsentingHouseholdEvent()
    }

}