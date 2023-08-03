package io.bewsys.spmobile.ui.households.detail


import android.util.Log
import androidx.lifecycle.*

import io.bewsys.spmobile.data.local.HouseholdModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class HouseholdDetailViewModel(
    private val state: SavedStateHandle
) : ViewModel() {
    private val _detailChannel = Channel<DetailEvent>()
    val detailChannel get() = _detailChannel.receiveAsFlow()

    var household: HouseholdModel? = null
    var id: Long? = null

    fun onEditFabClicked() {
        viewModelScope.launch {
            _detailChannel.send(DetailEvent.FabClicked)
        }
    }

    fun onDeleteActionFabClicked() {
        viewModelScope.launch {
            _detailChannel.send(DetailEvent.FabDeleteActionClicked(id!!))
        }
    }

    fun onEditActionFabClicked() {
        viewModelScope.launch {
            _detailChannel.send(DetailEvent.FabEditActionClicked(household!!))
        }
    }

    sealed class DetailEvent {
        object FabClicked : DetailEvent()
        data class FabEditActionClicked(val householdModel: HouseholdModel) : DetailEvent()
        data class FabDeleteActionClicked(val id: Long) : DetailEvent()

    }
}