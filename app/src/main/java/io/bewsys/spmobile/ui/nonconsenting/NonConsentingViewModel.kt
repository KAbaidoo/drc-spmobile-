package io.bewsys.spmobile.ui.nonconsenting


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NonConsentingViewModel (  private val state: SavedStateHandle): ViewModel() {

    private val nonConsentingEventChannel = Channel<NonConsentingEvent>()
    val nonConsentingEvent = nonConsentingEventChannel.receiveAsFlow()

    fun onFabClicked() = viewModelScope.launch{
      nonConsentingEventChannel.send(NonConsentingEvent.NavigateToNonConsentingHouseholdsForm)
    }

    sealed class NonConsentingEvent{
        object NavigateToNonConsentingHouseholdsForm: NonConsentingEvent()
    }
}