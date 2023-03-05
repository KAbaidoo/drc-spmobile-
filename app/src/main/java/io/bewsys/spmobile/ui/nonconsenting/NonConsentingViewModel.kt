package io.bewsys.spmobile.ui.nonconsenting


import androidx.lifecycle.*
import io.bewsys.spmobile.ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK
import io.bewsys.spmobile.data.local.NonConsentHouseholdModel
import io.bewsys.spmobile.data.repository.CommunityRepository
import io.bewsys.spmobile.data.repository.NonConsentingHouseholdRepository
import io.bewsys.spmobile.data.repository.ProvinceRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NonConsentingViewModel(
    private val nonConsentingHouseholdRepository: NonConsentingHouseholdRepository,
    private val provinceRepository: ProvinceRepository,
    private val communityRepository: CommunityRepository
) : ViewModel() {

    private val _nonConsentingEventChannel = Channel<NonConsentingEvent>()
    val nonConsentingEvent = _nonConsentingEventChannel.receiveAsFlow()

    private val _nonConsentingHouseholds = MutableLiveData<List<NonConsentHouseholdModel>>()
    val nonConsentingHouseholds: LiveData<List<NonConsentHouseholdModel>>
        get() = _nonConsentingHouseholds


    init {
        loadNonConsentingHouseholds()
    }


    private fun loadNonConsentingHouseholds() {
        viewModelScope.launch {
            nonConsentingHouseholdRepository.getAllNonConsentingHouseholds()
           .map { households ->
                households.map {
                    NonConsentHouseholdModel(
                        it.id,
                        it.province_id,
                        it.community_id,
                        it.province_id?.let { id -> provinceRepository.getById(id)?.name },
                        it.community_id?.let { id -> communityRepository.getById(id)?.name },
                        it.gps_longitude,
                        it.gps_latitude,
                        it.reason,
                        it.other_non_consent_reason,
                        it.status
                    )
                }  //add on each statement here to commit each to work manager

            }.collectLatest {
                    _nonConsentingHouseholds.value = it
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