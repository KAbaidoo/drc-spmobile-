package io.bewsys.spmobile.ui.nonconsenting


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.bewsys.spmobile.data.model.NonConsentHousehold
import io.bewsys.spmobile.data.repository.CommunityRepositoryImpl
import io.bewsys.spmobile.data.repository.NonConsentingHouseholdRepositoryImpl
import io.bewsys.spmobile.data.repository.ProvinceRepositoryImpl
import io.bewsys.spmobile.ui.ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NonConsentingViewModel(
    private val nonConsentingHouseholdRepositoryImpl: NonConsentingHouseholdRepositoryImpl,
    private val provinceRepositoryImpl: ProvinceRepositoryImpl,
    private val communityRepositoryImpl: CommunityRepositoryImpl
) : ViewModel() {

    private val _nonConsentingEventChannel = Channel<NonConsentingEvent>()
    val nonConsentingEvent = _nonConsentingEventChannel.receiveAsFlow()

    private val _nonConsentingHouseholds = MutableLiveData<List<NonConsentHousehold>>()
    val nonConsentingHouseholds: LiveData<List<NonConsentHousehold>>
        get() = _nonConsentingHouseholds


    init {
        loadNonConsentingHouseholds()
    }

    private fun loadNonConsentingHouseholds() {
        viewModelScope.launch {
            nonConsentingHouseholdRepositoryImpl.getAllNonConsentingHouseholds().collectLatest {
                _nonConsentingHouseholds.value = it.map { entity ->
                    NonConsentHousehold(
                        entity.id,
                        entity.province_id,
                        entity.community_id,
                        entity.province_id?.let { id -> provinceRepositoryImpl.getById(id)?.name },
                        entity.community_id?.let { id -> communityRepositoryImpl.getById(id)?.name },
                        entity.gps_longitude,
                        entity.gps_latitude,
                        entity.reason,
                        entity.other_non_consent_reason,
                        entity.status
                    )
                }
                //add on each statement here to commit each to work manager
            }
        }
    }


    fun onFabClicked() = viewModelScope.launch {
        _nonConsentingEventChannel.send(NonConsentingEvent.NavigateToNonConsentingHouseholdsForm)
    }

    fun onAddNonConsentingHouseholdResult(result: Int) {
        when (result) {
            ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK -> showNonConsentingHouseholdSavedConfirmationMessage()
        }
    }

    private fun showNonConsentingHouseholdSavedConfirmationMessage() =
        viewModelScope.launch {
            _nonConsentingEventChannel.send(
                NonConsentingEvent.ShowNonConsentingHouseholdSavedConfirmationMessage(
                    "Non-consenting household saved!"
                )
            )
        }

    sealed class NonConsentingEvent {
        object NavigateToNonConsentingHouseholdsForm : NonConsentingEvent()
        data class ShowNonConsentingHouseholdSavedConfirmationMessage(val msg: String) :
            NonConsentingEvent()
    }
}