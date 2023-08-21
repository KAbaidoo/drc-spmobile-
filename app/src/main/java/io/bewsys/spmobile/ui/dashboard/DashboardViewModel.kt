package io.bewsys.spmobile.ui.dashboard

import androidx.lifecycle.*
import io.bewsys.spmobile.LOGIN_RESULT_OK
import io.bewsys.spmobile.UPDATE_USER_RESULT_OK

import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.data.repository.MemberRepository
import io.bewsys.spmobile.util.PairMediatorLiveData
import io.bewsys.spmobile.util.Resource
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG = "Dashboard"

class DashboardViewModel(
    private val dashboardRepository: DashboardRepository,
    private val householdRepository: HouseholdRepository,
    private val membersRepository: MemberRepository,
) : ViewModel() {


    private val _dashboardEventChannel = Channel<DashboardEvent>()
    val dashboardEvent = _dashboardEventChannel.receiveAsFlow()

    private val provinceCount = dashboardRepository.provinceCountFlow.asLiveData()
    private val communityCount = dashboardRepository.communityCountFlow.asLiveData()
    private val territoryCount = dashboardRepository.territoryCountFlow.asLiveData()
    private val groupmentCount = dashboardRepository.groupementCountFlow.asLiveData()

    private val householdCount = householdRepository.getHouseHoldCount.asLiveData()
    private val membersCount = membersRepository.getMembersCount.asLiveData()

    val provinceAndCommunity = PairMediatorLiveData(provinceCount, communityCount)
    val territoryAndGroupement = PairMediatorLiveData(territoryCount, groupmentCount)
    val householdAndMembers = PairMediatorLiveData(householdCount, membersCount)


 /*   fun loadData() = viewModelScope.launch {
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
    }*/

    fun loadAllData() = viewModelScope.launch {
        coroutineScope {
            var provinceResponse: Flow<Resource<Any>>? = null
            var communityResponse: Flow<Resource<Any>>? = null
            var territoryResponse:  Flow<Resource<Any>>? = null
            var groupmentResponse:  Flow<Resource<Any>>? =  null
            var healthZoneResponse:  Flow<Resource<Any>>? = null
            var healthAreaResponse:  Flow<Resource<Any>>?=  null

            val provinceCall = async { dashboardRepository.fetchProvinceData() }
            val communityCall = async { dashboardRepository.fetchCommunitiesData() }
            val groupmentCall = async { dashboardRepository.fetchGroupmentsData() }
            val territoryCall = async { dashboardRepository.fetchTerritoriesData() }
            val healthZoneCall = async { dashboardRepository.fetchHealthZonesData() }
            val healthAreaCall = async { dashboardRepository.fetchHealthAreasData() }

            try {
                provinceCall.await()
                provinceResponse = provinceCall.await()
                 communityResponse = communityCall.await()
                 territoryResponse = territoryCall.await()
                 groupmentResponse = groupmentCall.await()
                 healthZoneResponse = healthZoneCall.await()
                 healthAreaResponse = healthAreaCall.await()

            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            processData(listOf(provinceResponse, communityResponse, territoryResponse, groupmentResponse, healthZoneResponse, healthAreaResponse))

        }


    }

    private suspend fun processData(list: List< Flow<Resource<Any>>?>){
        list.map {
            it?.collectLatest { results ->
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
            loadAllData()
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


