package io.bewsys.spmobile.ui.nonconsenting.form

import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import io.bewsys.spmobile.ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK

import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.data.CommunityEntity
import io.bewsys.spmobile.data.GroupmentEntity
import io.bewsys.spmobile.data.ProvinceEntity
import io.bewsys.spmobile.data.TerritoryEntity
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

    private var provinceId: String? = state.get<String>("province_id")
        set(value) {
            field = value
            state["province_id"] = value
        }
    private var communityId: String? = state.get<String>("community_id")
        set(value) {
            field = value
            state["community_id"] = value
        }

    private var groupmentId: String? = state.get<String>("groupment_id")
        set(value) {
            field = value
            state["groupment_id"] = value
        }
    private var territoryId: String? = state.get<String>("territory_id")
        set(value) {
            field = value
            state["territory_id"] = value
        }


    private fun loadProvinces() {
        viewModelScope.launch {
            dashboardRepository.getAllProvinces().map {
                it.map {
                    it.name!!
                }
            }.collect {
                _provinces.value = it
            }
        }
    }

    fun loadTerritories() {
        viewModelScope.launch {
            provinceId?.let {
                dashboardRepository.getTerritoriesByProvinceId(it).map {
                    it.map {
                        it.name!!
                    }
                }.collect {
                    _territories.value = it
                }
            }
        }
    }

    fun loadCommunities() {
        viewModelScope.launch {
            territoryId?.let {
                dashboardRepository.getCommunitiesByTerritoryId(it).map {
                    it.map {
                        it.name!!
                    }
                }.collect {
                    _communities.value = it
                }
            }
        }
    }

    fun loadGroupments() {
        viewModelScope.launch {
            communityId?.let {
                dashboardRepository.getGroupmentsByCommunityId(it).map {
                    it.map {
                        it.name!!
                    }
                }.collect {
                    _groupments.value = it
                }
            }
        }
    }


    private val provinceQuery = state.getStateFlow("province", "").flatMapLatest {
        dashboardRepository.getProvinceByName(it)
    }

    fun getProvinceId() {
        viewModelScope.launch {
            provinceQuery.collect {
                provinceId = it.firstOrNull()?.id.toString()
            }
        }
    }

    private val communityQuery = state.getStateFlow("community", "").flatMapLatest {
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