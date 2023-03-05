package io.bewsys.spmobile.ui.nonconsenting.form

import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import io.bewsys.spmobile.ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK

import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.data.CommunityEntity
import io.bewsys.spmobile.data.ProvinceEntity
import io.bewsys.spmobile.data.local.NonConsentHouseholdModel
import io.bewsys.spmobile.data.repository.CommunityRepository
import io.bewsys.spmobile.data.repository.NonConsentingHouseholdRepository
import io.bewsys.spmobile.data.repository.ProvinceRepository
import io.bewsys.spmobile.work.UploadWorker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class AddNonConsentingHouseholdViewModel(
    application: Application,
    private val state: SavedStateHandle,
    private val nonConsentingHouseholdRepository: NonConsentingHouseholdRepository,
    private val provinceRepository: ProvinceRepository,
    private val communityRepository: CommunityRepository
) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)


    private val _provinces = MutableLiveData<List<ProvinceEntity>>()
    val provinces: LiveData<List<ProvinceEntity>>
        get() = _provinces

    private val _communities = MutableLiveData<List<CommunityEntity>>()
    val communities: LiveData<List<CommunityEntity>>
        get() = _communities


    init {
        loadProvinces()
        loadCommunities()
    }

    private fun loadProvinces() {
        viewModelScope.launch {
            provinceRepository.getAllProvinces().collectLatest {
                _provinces.value = it
            }
        }
    }

    private fun loadCommunities() {
        viewModelScope.launch {
            communityRepository.getAllCommunities().collectLatest {
                _communities.value = it
            }
        }
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

    private var provinceId: Long? = state.get<Long>("province_id")
        set(value) {
            field = value
            state["province_id"] = value
        }
    private var communityId: Long? = state.get<Long>("community_id")
        set(value) {
            field = value
            state["community_id"] = value
        }

    private val provinceQuery = state.getStateFlow("province", "").flatMapLatest {
        provinceRepository.getByName(it)
    }


    fun getProvinceId() {
        viewModelScope.launch {
            provinceQuery.collect {
                provinceId = it.firstOrNull()?.id
            }
        }
    }

    private val communityQuery = state.getStateFlow("community", "").flatMapLatest {
        communityRepository.getByName(it)
    }

    fun getCommunityId() {
        viewModelScope.launch {
            communityQuery.collect {
                communityId = it.firstOrNull()?.id
            }
        }
    }

    fun onRegisterClicked() {
        if (reason.isBlank() || otherReason.isBlank() || province.isBlank() || territory.isBlank() || community.isBlank() || groupment.isBlank()) {
            showInvalidInputMessage()
            return
        } else {
            NonConsentHouseholdModel(
                province_id = provinceId,
                community_id = communityId,
                gps_latitude = lat,
                gps_longitude = lon,
                reason = reason,
                other_non_consent_reason = otherReason
            ).also {
                addNonConsentingHousehold(it)
            }
        }
    }

    private fun addNonConsentingHousehold(newNonConsentingHousehold: NonConsentHouseholdModel) =
        viewModelScope.launch {

            nonConsentingHouseholdRepository.insertNonConsentingHousehold(
                newNonConsentingHousehold
            )

            uploadNonConsentingHousehold(nonConsentingHouseholdRepository.getLastInsertedRowId())

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

        val uploadRequest = OneTimeWorkRequestBuilder<UploadWorker>()
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