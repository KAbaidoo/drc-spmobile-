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


