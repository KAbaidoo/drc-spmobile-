package io.bewsys.spmobile.ui.nonconsenting.forms

import android.util.Log
import androidx.lifecycle.*
import app.cash.sqldelight.db.QueryResult.Unit.value
import io.bewsys.spmobile.data.CommunityEntity
import io.bewsys.spmobile.data.ProvinceEntity
import io.bewsys.spmobile.data.model.NonConsentHousehold
import io.bewsys.spmobile.data.repository.CommunityRepositoryImpl
import io.bewsys.spmobile.data.repository.NonConsentingHouseholdRepositoryImpl
import io.bewsys.spmobile.data.repository.ProvinceRepositoryImpl
import io.bewsys.spmobile.ui.ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddNonConsentingHouseholdViewModel(
    private val state: SavedStateHandle,
    private val nonConsentingHouseholdRepositoryImpl: NonConsentingHouseholdRepositoryImpl,
    private val provinceRepositoryImpl: ProvinceRepositoryImpl,
    private val communityRepositoryImpl: CommunityRepositoryImpl
) : ViewModel() {

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
            provinceRepositoryImpl.getAllProvinces().collectLatest {
                _provinces.value = it
            }
        }
    }

    private fun loadCommunities() {
        viewModelScope.launch {
            communityRepositoryImpl.getAllCommunities().collectLatest {
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
        provinceRepositoryImpl.getByName(it)
    }

    fun getProvinceId() {
        viewModelScope.launch {
            provinceQuery.collect {
                provinceId = it.firstOrNull()?.id
            }
        }
    }

    private val communityQuery = state.getStateFlow("community", "").flatMapLatest {
        communityRepositoryImpl.getByName(it)
    }

    fun getCommunityId() {
        viewModelScope.launch {
            communityQuery.collect {
                communityId = it.firstOrNull()?.id
            }
        }
    }

    fun onRegisterClicked() {
        if (reason.isBlank() || otherReason.isBlank() || province.isBlank() || territory.isBlank() || community.isBlank() || groupment.isBlank() || address.isBlank() /*|| lon.isBlank() || lat.isBlank()*/) {
            showInvalidInputMessage()
            return
        } else {
            NonConsentHousehold(
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

    private fun addNonConsentingHousehold(newNonConsentingHousehold: NonConsentHousehold) =
        viewModelScope.launch {

            nonConsentingHouseholdRepositoryImpl.insertNonConsentingHousehold(
                newNonConsentingHousehold
            )

            addNonConsentingHouseholdChannel.send(
                AddNonConsentingHouseholdEvent.NavigateBackWithResults(
                    ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK
                )
            )
        }

    private fun showInvalidInputMessage() = viewModelScope.launch {
        addNonConsentingHouseholdChannel.send(
            AddNonConsentingHouseholdEvent.ShowInvalidInputMessage(
                "One or more text fields might be empty!"
            )
        )
    }


    sealed class AddNonConsentingHouseholdEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddNonConsentingHouseholdEvent()
        data class NavigateBackWithResults(val results: Int) : AddNonConsentingHouseholdEvent()
    }


}