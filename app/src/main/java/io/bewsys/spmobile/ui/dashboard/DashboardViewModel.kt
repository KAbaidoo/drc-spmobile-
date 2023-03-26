package io.bewsys.spmobile.ui.dashboard

import androidx.lifecycle.*
import io.bewsys.spmobile.LOGIN_RESULT_OK
import io.bewsys.spmobile.UPDATE_USER_RESULT_OK

import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.util.PairMediatorLiveData
import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "Dashboard"

class DashboardViewModel(
    private val dashboardRepository: DashboardRepository,
    private val householdRepository: HouseholdRepository
) : ViewModel() {
   /* private val _provinceCount = MutableLiveData(0)
    private val _communityCount = MutableLiveData(0)
    private val _territoriesCount = MutableLiveData(0)
    private val _groupementsCount = MutableLiveData(0)
    private val _householdCount = MutableLiveData(0)
    private val _membersCount = MutableLiveData(0)




    val provinceAndCommunity = PairMediatorLiveData(_provinceCount, _communityCount)
    val territoryAndGroupement = PairMediatorLiveData(_territoriesCount, _groupementsCount)
    val householdAndMembers = PairMediatorLiveData(_householdCount, _membersCount) */

    private val _dashboardEventChannel = Channel<DashboardEvent>()
    val dashboardEvent = _dashboardEventChannel.receiveAsFlow()

    private val provinceCount =  dashboardRepository.provinceCountFlow.asLiveData()
    private val communityCount =  dashboardRepository.communityCountFlow.asLiveData()
    private val territoryCount =  dashboardRepository.territoryCountFlow.asLiveData()
    private val groupmentCount =  dashboardRepository.groupementCountFlow.asLiveData()

    private val householdCount =  householdRepository.getHouseHoldCount.asLiveData()
    private val membersCount =  householdRepository.getMembersCount.asLiveData()

    val provinceAndCommunity = PairMediatorLiveData(provinceCount, communityCount)
    val territoryAndGroupement = PairMediatorLiveData(territoryCount, groupmentCount)
    val householdAndMembers = PairMediatorLiveData(householdCount, membersCount)




/*
    init {
//        loadCommunityCount()
        loadProvinceCount()
//        loadGroupmentCount()
//        loadTerritoriesCount()
    }


    fun loadProvinceCount() = viewModelScope.launch {

        launch {
            dashboardRepository.getAllProvinces().collectLatest {
                _provinceCount.value = withContext(Dispatchers.Default) { it.count() }
            }
        }

        launch {
            dashboardRepository.getAllCommunities().collectLatest {
                _communityCount.value = withContext(Dispatchers.Default) {
                    it.count()
                }
            }
        }
        launch {
            dashboardRepository.getAllTerritories().collectLatest {
                _territoriesCount.value = withContext(Dispatchers.Default) { it.count() }
            }
        }
        /* launch {
            dashboardRepository.getAllGroupments().collectLatest {
                _groupementsCount.value = withContext(Dispatchers.Default) { it.count() }
                showDashboardUpdatedSuccessfulMessage()
            }

        } */
        launch {
            householdRepository.getAllHouseholds().collectLatest {
                _householdCount.value = withContext(Dispatchers.Default) { it.count() }
            }
        }

        launch {
            householdRepository.getAllMemebers().collectLatest {
                _membersCount.value = withContext(Dispatchers.Default) { it.count() }
            }
        }

    }


    fun loadCommunityCount() = viewModelScope.launch {

    }

    fun loadTerritoriesCount() = viewModelScope.launch {


    }

    fun loadGroupmentCount() = viewModelScope.launch {

    } */

    fun loadData() = viewModelScope.launch {
        dashboardRepository.fetchData().collectLatest { results ->

            when (results) {
                is Resource.Loading -> _dashboardEventChannel.send(DashboardEvent.Loading)
                is Resource.Success -> _dashboardEventChannel.send(DashboardEvent.Successful)
                is Resource.Failure -> {
                    _dashboardEventChannel.send(DashboardEvent.Failure)
                }
                is Resource.Exception -> {
                    results.throwable.localizedMessage?.let { errorMsg ->
                        DashboardEvent.Exception(
                            errorMsg
                        )
                    }?.let { _dashboardEventChannel.send(it) }
                }
            }
        }
    }

    fun onResult(result: Int) {
        when (result) {
            UPDATE_USER_RESULT_OK -> showUpdateSuccessfulMessage()
            LOGIN_RESULT_OK -> showLoginSuccessfulMessage()
        }
    }


    private fun showLoginSuccessfulMessage() =
        viewModelScope.launch {
            _dashboardEventChannel.send(
                DashboardEvent.ShowMessage(
                    "Login Successful!"
                )
            )
            loadData()
        }

    private fun showUpdateSuccessfulMessage() =
        viewModelScope.launch {
            _dashboardEventChannel.send(
                DashboardEvent.ShowMessage(
                    "User updated Successfully!"
                )
            )
        }

    fun showDashboardUpdatedSuccessfulMessage() =
        viewModelScope.launch {
            delay(500L)
            _dashboardEventChannel.send(
                DashboardEvent.ShowMessage(
                    "Dashboard updated Successfully!"
                )
            )
        }


    sealed class DashboardEvent {
        data class ShowMessage(val msg: String) : DashboardEvent()

        //    data class ShowUpdateSuccessfulMessage(val msg: String) : DashboardEvent()
        object Failure : DashboardEvent()
        object Successful : DashboardEvent()
        data class Exception(val errorMsg: String) : DashboardEvent()
        object Loading : DashboardEvent()


    }


}


