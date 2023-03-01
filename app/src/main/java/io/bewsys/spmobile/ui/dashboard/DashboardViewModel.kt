package io.bewsys.spmobile.ui.dashboard

import android.util.Log
import androidx.lifecycle.*
import app.cash.sqldelight.db.QueryResult.Unit.value

import io.bewsys.spmobile.data.repository.CommunityRepositoryImpl
import io.bewsys.spmobile.data.repository.ProvinceRepositoryImpl
import io.bewsys.spmobile.prefsstore.PreferencesManager
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingViewModel
import io.bewsys.spmobile.util.PairMediatorLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val TAG = "Dashboard"

class DashboardViewModel(
    private val provinceRepositoryImpl: ProvinceRepositoryImpl,
    private val communityRepositoryImpl: CommunityRepositoryImpl,
    private val preferencesManager: PreferencesManager

) : ViewModel() {
    private val _dashboardEvent = Channel<DashboardEvent>()
    val dashboardEvent = _dashboardEvent.receiveAsFlow()


    private val _provinceCount = MutableLiveData(0)
    private val _communityCount = MutableLiveData(0)

    val counts = PairMediatorLiveData(_provinceCount, _communityCount)

    init {
        loadCommunityCount()
        loadProvinceCount()
    }

    val prefs = preferencesManager.preferencesFlow.asLiveData()

    private fun loadProvinceCount() = viewModelScope.launch {
        provinceRepositoryImpl.getAllProvinces().collectLatest {
            _provinceCount.value = it.count()
        }
    }

    private fun loadCommunityCount() = viewModelScope.launch {
        communityRepositoryImpl.getAllCommunities().collectLatest {
            _communityCount.value = it.count()
        }
    }

    fun navigateToLoginScreen() = viewModelScope.launch {
        _dashboardEvent.send(DashboardEvent.NavigateToLogin)
    }

    sealed class DashboardEvent {
        object NavigateToLogin : DashboardEvent()

    }
}


