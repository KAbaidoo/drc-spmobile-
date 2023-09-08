package io.bewsys.spmobile.ui.dashboard

import androidx.lifecycle.*
import io.bewsys.spmobile.LOGIN_RESULT_OK
import io.bewsys.spmobile.UPDATE_USER_RESULT_OK

import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.data.repository.MemberRepository
import io.bewsys.spmobile.ui.common.BaseViewModel
import io.bewsys.spmobile.util.PairMediatorLiveData
import io.bewsys.spmobile.util.Resource
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

private const val TAG = "Dashboard"

class DashboardViewModel(
    private val dashboardRepository: DashboardRepository,
    private val householdRepository: HouseholdRepository,
    private val membersRepository: MemberRepository,
) : BaseViewModel<Unit>() {


    val provinceAndCommunity = PairMediatorLiveData(
        dashboardRepository.provinceCountFlow.asLiveData(),
        dashboardRepository.communityCountFlow.asLiveData()
    )
    val territoryAndGroupement = PairMediatorLiveData(
        dashboardRepository.territoryCountFlow.asLiveData(),
        dashboardRepository.groupementCountFlow.asLiveData()
    )
    val householdAndMembers = PairMediatorLiveData(
        householdRepository.getHouseHoldCount.asLiveData(),
        membersRepository.getMembersCount.asLiveData()
    )


    private fun loadAllData() = viewModelScope.launch {
        coroutineScope {
            var provinceResponse: Flow<Resource<Any>>? = null
            var communityResponse: Flow<Resource<Any>>? = null
            var territoryResponse: Flow<Resource<Any>>? = null
            var groupmentResponse: Flow<Resource<Any>>? = null
            var healthZoneResponse: Flow<Resource<Any>>? = null
            var healthAreaResponse: Flow<Resource<Any>>? = null

            val provinceCall = async { dashboardRepository.fetchProvinceData() }
            val communityCall = async { dashboardRepository.fetchCommunitiesData() }
            val groupmentCall = async { dashboardRepository.fetchGroupmentsData() }
            val territoryCall = async { dashboardRepository.fetchTerritoriesData() }
            val healthZoneCall = async { dashboardRepository.fetchHealthZonesData() }
            val healthAreaCall = async { dashboardRepository.fetchHealthAreasData() }

            try {
                provinceResponse = provinceCall.await()
                communityResponse = communityCall.await()
                territoryResponse = territoryCall.await()
                groupmentResponse = groupmentCall.await()
                healthZoneResponse = healthZoneCall.await()
                healthAreaResponse = healthAreaCall.await()

            } catch (ex: Exception) {
                ex.printStackTrace()
            }



            processData(
                listOf(
                    provinceResponse,
                    communityResponse,
                    territoryResponse,
                    groupmentResponse,
                    healthZoneResponse,
                    healthAreaResponse
                )
            )

        }


    }

    private suspend fun processData(list: List<Flow<Resource<Any>>?>) {


        list.map {
            it?.collectLatest { results ->
                when (results) {
                    is Resource.Loading -> _eventChannel.send(Event.Loading)
                    is Resource.Success -> _eventChannel.send(Event.Successful(1))
                    is Resource.Failure -> {
                        _eventChannel.send(Event.Error("Something went wrong!"))
                    }

                    is Resource.Exception -> {
                        results.throwable.localizedMessage?.let { errorMsg ->
                            Event.Error(
                                errorMsg
                            )
                        }?.let { _eventChannel.send(it) }
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
            _eventChannel.send(
                Event.ShowSnackBar(
                    "Login Successful!"
                )
            )
            loadAllData()
        }

    private fun showUpdateSuccessfulMessage() =
        viewModelScope.launch {
            _eventChannel.send(
                Event.ShowSnackBar(
                    "User updated Successfully!"
                )
            )
        }

    fun showDashboardUpdatedSuccessfulMessage() =
        viewModelScope.launch {
            delay(500L)
            _eventChannel.send(
                Event.ShowSnackBar(
                    "Dashboard updated Successfully!"
                )
            )
        }


}


