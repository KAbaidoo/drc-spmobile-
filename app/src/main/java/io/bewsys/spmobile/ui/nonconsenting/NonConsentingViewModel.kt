package io.bewsys.spmobile.ui.nonconsenting


import android.app.Application
import androidx.lifecycle.*
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import io.bewsys.spmobile.ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK
import io.bewsys.spmobile.data.local.NonConsentHouseholdModel
import io.bewsys.spmobile.data.repository.NonConsentingHouseholdRepository
import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.work.NonConsentUploadWorker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NonConsentingViewModel(
    private val nonConsentingHouseholdRepository: NonConsentingHouseholdRepository,
    private val dashboardRepository: DashboardRepository
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
                            it.territory_id,
                            it.community_id,
                            it.groupement_id,
                            it.province_id?.let { id -> dashboardRepository.getProvinceById(id.toLong())?.name } ,
                            it.community_id?.let { id -> dashboardRepository.getCommunityById(id.toLong())?.name },
                            it.territory_id?.let { id -> dashboardRepository.getTerritoryById(id.toLong())?.name },
                            it.groupement_id?.let { id -> dashboardRepository.getGroupmentById(id.toLong())?.name },
                            it.gps_longitude,
                            it.gps_latitude,
                            it.reason,
                            it.address,
                            it.other_non_consent_reason,
                            it.status

                        )
                    }  //add on each statement here to commit each to work manager

                }.collectLatest {
                    _nonConsentingHouseholds.value = it
                }

        }
    }
//    ?.let { id -> dashboardRepository.getProvinceById(id.toLong())?.name }

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

    fun onHousholdSelected(nonConsentingHousehold: NonConsentHouseholdModel)= viewModelScope.launch {
        _nonConsentingEventChannel.send(NonConsentingEvent.NavigateToEditNonConsentingHouseholdsForm(nonConsentingHousehold))
    }




    sealed class NonConsentingEvent {
        object NavigateToNonConsentingHouseholdsForm : NonConsentingEvent()
        data class NavigateToEditNonConsentingHouseholdsForm(val nonConsentingHousehold: NonConsentHouseholdModel) : NonConsentingEvent()
        data class ShowNonConsentingHouseholdSavedConfirmationMessage(val msg: String) :
            NonConsentingEvent()
    }
}