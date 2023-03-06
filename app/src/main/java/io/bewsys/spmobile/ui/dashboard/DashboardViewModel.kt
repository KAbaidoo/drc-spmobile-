package io.bewsys.spmobile.ui.dashboard

import androidx.lifecycle.*
import io.bewsys.spmobile.ADD_NON_CONSENTING_HOUSEHOLD_RESULT_OK
import io.bewsys.spmobile.LOGIN_RESULT_OK
import io.bewsys.spmobile.UPDATE_USER_RESULT_OK

import io.bewsys.spmobile.data.repository.CommunityRepository
import io.bewsys.spmobile.data.repository.ProvinceRepository
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.repository.UserRepository
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingViewModel
import io.bewsys.spmobile.util.PairMediatorLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG = "Dashboard"

class DashboardViewModel(
    private val provinceRepository: ProvinceRepository,
    private val communityRepository: CommunityRepository

) : ViewModel() {
    private val _dashboardEventChannel = Channel<DashboardEvent>()
    val dashboardEvent = _dashboardEventChannel.receiveAsFlow()


    private val _provinceCount = MutableLiveData(0)
    private val _communityCount = MutableLiveData(0)

    val counts = PairMediatorLiveData(_provinceCount, _communityCount)

    init {
        loadCommunityCount()
        loadProvinceCount()
    }


    private fun loadProvinceCount() = viewModelScope.launch {
        provinceRepository.getAllProvinces().collectLatest {
            _provinceCount.value = it.count()
        }
    }

    private fun loadCommunityCount() = viewModelScope.launch {
        communityRepository.getAllCommunities().collectLatest {
            _communityCount.value = it.count()
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
                DashboardEvent.ShowLoginSuccessfulMessage(
                    "Login Successful!"
                )
            )
        }

    private fun showUpdateSuccessfulMessage() =
        viewModelScope.launch {
            _dashboardEventChannel.send(
                DashboardEvent.ShowUpdateSuccessfulMessage(
                    "User updated Successfully!"
                )
            )
        }


    sealed class DashboardEvent {
        data class ShowLoginSuccessfulMessage(val msg: String) : DashboardEvent()
        data class ShowUpdateSuccessfulMessage(val msg: String) : DashboardEvent()

    }


}


