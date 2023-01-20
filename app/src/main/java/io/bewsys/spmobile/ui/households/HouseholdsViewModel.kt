package io.bewsys.spmobile.ui.households

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HouseholdsViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle) : ViewModel() {

    private val householdsEventChannel = Channel<HouseholdEvent>()
    val householdsEventEvent = householdsEventChannel.receiveAsFlow()

    fun onAddRegistrationFabClicked() = viewModelScope.launch{
        householdsEventChannel.send(HouseholdEvent.AddRegistrationClicked)
    }

    fun onHumanitarianFabClicked() = viewModelScope.launch {
       householdsEventChannel.send(HouseholdEvent.HumanitarianClicked)
    }

    fun onDevelopmentalFabClicked() = viewModelScope.launch {
        householdsEventChannel.send(HouseholdEvent.DevelopmentalClicked)
    }

    sealed class HouseholdEvent{
        object AddRegistrationClicked: HouseholdEvent()
        object DevelopmentalClicked: HouseholdEvent()
        object HumanitarianClicked: HouseholdEvent()
    }
}