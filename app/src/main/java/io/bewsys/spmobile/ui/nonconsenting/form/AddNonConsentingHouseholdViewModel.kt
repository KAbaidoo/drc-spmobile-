package io.bewsys.spmobile.ui.nonconsenting.form

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.work.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import io.bewsys.spmobile.ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK

import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.PERMISSION_LOCATION_REQUEST_CODE
import io.bewsys.spmobile.data.local.HouseholdModel

import io.bewsys.spmobile.data.local.NonConsentHouseholdModel
import io.bewsys.spmobile.data.repository.NonConsentingHouseholdRepository
import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.work.NonConsentUploadWorker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class AddNonConsentingHouseholdViewModel(
    application: Application,
    private val state: SavedStateHandle,
    private val nonConsentingRepository: NonConsentingHouseholdRepository,
    private val dashboardRepository: DashboardRepository
) : ViewModel() {
    init {
        loadProvinces()
    }

    var household: NonConsentHouseholdModel? = null
    var id: Long? = null

    private val workManager = WorkManager.getInstance(application)

    private val addNonConsentingHouseholdChannel = Channel<AddNonConsentingHouseholdEvent>()
    val addNonConsentingHouseholdEvent = addNonConsentingHouseholdChannel.receiveAsFlow()

    private val _provinces = MutableLiveData<List<String>>()
    val provinces: LiveData<List<String>>
        get() = _provinces

    private val _communities = MutableLiveData<List<String>>()
    val communities: LiveData<List<String>>
        get() = _communities

    private val _territories = MutableLiveData<List<String>>()
    val territories: LiveData<List<String>>
        get() = _territories

    private val _groupments = MutableLiveData<List<String>>()
    val groupments: LiveData<List<String>>
        get() = _groupments


    var reason = ""
    var otherReason = ""
    var address = ""


    var lon: String? = ""
        set(value) {
            state["lon"] = value
            field = value
        }
        get() = state["lon"]

    var lat: String? = ""
        set(value) {
            state["lat"] = value
            field = value
        }
        get() = state["lat"]

    var province = ""
    var territory = ""
    var community = ""
    var groupment = ""


    private var provinceId: String? = "1"
        set(value) {
            field = value
            state["province_id"] = value
        }
        get() = state["province_id"]

    private var communityId: String? = "1"
        set(value) {
            field = value
            state["community_id"] = value
        }
        get() = state["community_id"]

    private var groupmentId: String? = "1"
        set(value) {
            field = value
            state["groupment_id"] = value
        }
        get() = state["groupment_id"]

    private var territoryId: String? = "1"
        set(value) {
            field = value
            state["territory_id"] = value
        }
        get() = state["territory_id"]

    fun loadProvinces() {
        viewModelScope.launch {
            dashboardRepository.getProvincesList().collectLatest {
                _provinces.value = it
            }
        }

    }

    fun getGroupmentId(groupmentName: String) {
        viewModelScope.launch {
            dashboardRepository.getGroupmentByName(groupmentName).collectLatest { list ->
                list.firstOrNull()?.apply {
                    groupmentId = id.toString()

                }


            }
        }
    }

    fun loadTerritoriesWithName(provinceName: String) {
        viewModelScope.launch {
            dashboardRepository.getProvinceByName(provinceName).collectLatest {
                it.firstOrNull()?.apply {
                    provinceId = id.toString()

                }
                dashboardRepository.getTerritoriesList(provinceId!!)
                    .collectLatest {
                        _territories.value = it
                    }
            }
        }
    }

    fun loadCommunitiesWithName(territoryName: String) {
        viewModelScope.launch {
            dashboardRepository.getTerritoryByName(territoryName).collectLatest {
                it.firstOrNull()?.apply {
                    territoryId = id.toString()

                }
                dashboardRepository.getCommunitiesList(territoryId!!)
                    .collectLatest {
                        _communities.value = it
                    }
            }

        }
    }

    fun loadGroupmentsWithName(communityName: String) {
        viewModelScope.launch {
            dashboardRepository.getCommunityByName(communityName).collectLatest {
                it.firstOrNull()?.apply {
                    communityId = id.toString()

                }
                dashboardRepository.getGroupmentsList(communityId!!)
                    .collectLatest {
                        _groupments.value = it
                    }
            }

        }
    }


    fun onSaveClicked() {
        if (reason.isBlank() || address.isBlank()) {
            showInvalidInputMessage()
            return
        }

        if (household != null) {
            updateHousehold()
        } else {
            registerHousehold()
        }
    }

    private fun registerHousehold() {
        NonConsentHouseholdModel(
            province_id = provinceId,
            community_id = communityId,
            territory_id = territoryId,
            groupement_id = groupmentId,
            province_name = province,
            community_name = community,
            territory_name = territory,
            groupement_name = groupment,
            gps_latitude = lat,
            gps_longitude = lon,
            reason = reason,
            address = address,
            other_non_consent_reason = otherReason,
        ).also {
            addNonConsentingHousehold(it)
        }
    }

    private fun updateHousehold() {
        NonConsentHouseholdModel(
            province_id = provinceId,
            community_id = communityId,
            territory_id = territoryId,
            groupement_id = groupmentId,
            province_name = province,
            community_name = community,
            territory_name = territory,
            groupement_name = groupment,
            gps_latitude = lat,
            gps_longitude = lon,
            reason = reason,
            address = address,
            other_non_consent_reason = otherReason,
        ).also {
            id?.let { id -> updateNonConsentingHousehold(id, it) }
        }
    }

    private fun addNonConsentingHousehold(newNonConsentingHousehold: NonConsentHouseholdModel) =
        viewModelScope.launch {

            nonConsentingRepository.insertNonConsentingHousehold(
                newNonConsentingHousehold
            )

            uploadNonConsentingHousehold(nonConsentingRepository.getLastInsertedRowId())

//            lastly
            addNonConsentingHouseholdChannel.send(
                AddNonConsentingHouseholdEvent.NavigateBackWithResults(
                    ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK
                )
            )

        }

    private fun updateNonConsentingHousehold(
        id: Long,
        newNonConsentingHousehold: NonConsentHouseholdModel
    ) =
        viewModelScope.launch {

            nonConsentingRepository.updateNonConsentingHousehold(
                id,
                newNonConsentingHousehold
            )

            uploadNonConsentingHousehold(nonConsentingRepository.getLastInsertedRowId())

//            lastly
            addNonConsentingHouseholdChannel.send(
                AddNonConsentingHouseholdEvent.NavigateBackWithResults(
                    ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK
                )
            )

        }

    private fun uploadNonConsentingHousehold(itemId: Long) = viewModelScope.launch {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequestBuilder<NonConsentUploadWorker>()
            .setConstraints(constraints)
            .setInputData(createInputDataForId(itemId))
            .build()
        workManager.enqueue(uploadRequest)
    }

    private fun createInputDataForId(id: Long): Data {
        val builder = Data.Builder()
        builder.putLong(KEY_DATA_ID, id)
        return builder.build()
    }

    private fun showInvalidInputMessage() = viewModelScope.launch {
        addNonConsentingHouseholdChannel.send(
            AddNonConsentingHouseholdEvent.ShowInvalidInputMessage(
                "One or more fields empty!"
            )
        )
    }


    sealed class AddNonConsentingHouseholdEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddNonConsentingHouseholdEvent()
        data class NavigateBackWithResults(val results: Int) : AddNonConsentingHouseholdEvent()
    }


}
