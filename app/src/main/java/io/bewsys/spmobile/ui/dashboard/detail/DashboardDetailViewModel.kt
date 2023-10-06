package io.bewsys.spmobile.ui.dashboard.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import io.bewsys.spmobile.COMMUNITIES
import io.bewsys.spmobile.GROUPMENTS
import io.bewsys.spmobile.HOUSEHOLDS
import io.bewsys.spmobile.PROVINCES
import io.bewsys.spmobile.TERRITORIES
import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.ui.common.BaseViewModel

class DashboardDetailViewModel(private val dashboardRepository: DashboardRepository) :
    BaseViewModel<Unit>() {

    fun communities() =
        dashboardRepository.communitiesFlow().cachedIn(viewModelScope)
    fun provinces() = dashboardRepository.provincesFlow().cachedIn(viewModelScope)
    fun groupments() = dashboardRepository.groupmentsFlow().cachedIn(viewModelScope)
    fun territories() = dashboardRepository.territoriesFlow().cachedIn(viewModelScope)
    fun households() = dashboardRepository.householdsFlow().cachedIn(viewModelScope)
    fun members() = dashboardRepository.membersFlow().cachedIn(viewModelScope)


}