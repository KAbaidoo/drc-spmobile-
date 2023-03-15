package io.bewsys.spmobile.ui.dashboard

import androidx.lifecycle.*
import io.bewsys.spmobile.LOGIN_RESULT_OK
import io.bewsys.spmobile.UPDATE_USER_RESULT_OK

import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.util.PairMediatorLiveData
import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG = "Dashboard"

class DashboardViewModel(
    private val repository: DashboardRepository

) : ViewModel() {
    private val _dashboardEventChannel = Channel<DashboardEvent>()
    val dashboardEvent = _dashboardEventChannel.receiveAsFlow()


    private val _provinceCount = MutableLiveData(0)
    private val _communityCount = MutableLiveData(0)
    private val _territoriesCount = MutableLiveData(0)
    private val _groupementsCount = MutableLiveData(0)

    val provinceAndCommunity = PairMediatorLiveData(_provinceCount, _communityCount)
    val territoryAndGroupement = PairMediatorLiveData(_territoriesCount, _groupementsCount)

    init {
        loadData()
    }


     fun loadProvinceCount() = viewModelScope.launch {
        repository.getAllProvinces().collectLatest {
            _provinceCount.value = it.count()
        }
    }

     fun loadCommunityCount() = viewModelScope.launch {
        repository.getAllCommunities().collectLatest {
            _communityCount.value = it.count()
        }
    }
     fun loadTerritoriesCount() = viewModelScope.launch {
        repository.getAllTerritories().collectLatest {
            _territoriesCount.value = it.count()
        }
    }
     fun loadGroupmentCount() = viewModelScope.launch {
        repository.getAllGroupments().collectLatest {
            _groupementsCount.value = it.count()
        }
    }

    fun loadData() = viewModelScope.launch {
        repository.fetchData().collectLatest { results ->

            when (results) {
                is Resource.Loading -> _dashboardEventChannel.send(DashboardEvent.Loading)
                is Resource.Success -> _dashboardEventChannel.send(DashboardEvent.Successful)
                is Resource.Failure -> {
                    _dashboardEventChannel.send(DashboardEvent.Failure)
                }
                is Resource.Exception -> {
                    results.throwable.localizedMessage?.let { errorMsg->
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


