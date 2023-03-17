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


    init {
        loadProvinces()
//        loadCommunities()
    }


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
            getProvinceId()
            loadTerritories()
        }
    var territory = state.get<String>("territory") ?: ""
        set(value) {
            field = value
            state["territory"] = value
            getTerritoryId()
            loadCommunities()
        }
    var community = state.get<String>("community") ?: ""
        set(value) {
            field = value
            state["community"] = value
            getCommunityId()
            loadGroupments()
        }
    var groupment = state.get<String>("groupment") ?: ""
        set(value) {
            field = value
            state["groupment"] = value
            getGroupmentId()
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

    private var provinceId: String = state.get<String>("province_id")?: "1"
        set(value) {
            field = value
            state["province_id"] = value
        }
    private var communityId: String = state.get<String>("community_id")?: "1"
        set(value) {
            field = value
            state["community_id"] = value
        }

    private var groupmentId: String = state.get<String>("groupment_id")?: "1"
        set(value) {
            field = value
            state["groupment_id"] = value
        }
    private var territoryId: String = state.get<String>("territory_id")?: "1"
        set(value) {
            field = value
            state["territory_id"] = value
        }


    private fun loadProvinces() {
        viewModelScope.launch {
            dashboardRepository.getProvincesList().collectLatest {
                _provinces.value = it
            }
        }
    }




    fun loadTerritories() {
        viewModelScope.launch {
            dashboardRepository.getTerritoriesList(provinceId).collectLatest {
                _territories.value = it
            }
        }
    }



    fun loadCommunities() {
        viewModelScope.launch {
            dashboardRepository.getCommunitiesList(territoryId).collectLatest {
                _communities.value = it
            }
        }
    }


    fun loadGroupments() {
        viewModelScope.launch {
            dashboardRepository.getGroupmentsList(communityId).collectLatest {
                _groupments.value = it
            }
        }
    }

    val provinceQuery = state.getStateFlow("province", "").flatMapLatest {
        dashboardRepository.getProvinceByName(it)
    }

    fun getProvinceId() {
        viewModelScope.launch {
            provinceQuery.collect {
                provinceId = it.firstOrNull()?.id.toString()
            }
        }
    }

    val communityQuery = state.getStateFlow("community", "").flatMapLatest {
        dashboardRepository.getCommunityByName(it)
    }

    fun getCommunityId() {
        viewModelScope.launch {
            communityQuery.collect {
                communityId = it.firstOrNull()?.id.toString()
            }
        }
    }

    private val territoryQuery = state.getStateFlow("territory", "").flatMapLatest {
        dashboardRepository.getTerritoryByName(it)
    }

    fun getTerritoryId() {
        viewModelScope.launch {
            territoryQuery.collect {
                territoryId = it.firstOrNull()?.id.toString()
            }
        }
    }

    private val groupmentQuery = state.getStateFlow("groupment", "").flatMapLatest {
        dashboardRepository.getGroupmentByName(it)
    }

    fun getGroupmentId() {
        viewModelScope.launch {
            groupmentQuery.collect {
                groupmentId = it.firstOrNull()?.id.toString()
            }
        }
    }

    fun onRegisterClicked() {
        if (reason.isBlank() || province.isBlank() || territory.isBlank() || community.isBlank() || groupment.isBlank() || address.isBlank()) {
            showInvalidInputMessage()
            return
        } else {
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
