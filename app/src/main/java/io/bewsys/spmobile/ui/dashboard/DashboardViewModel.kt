package io.bewsys.spmobile.ui.dashboard

import androidx.lifecycle.*
import app.cash.sqldelight.db.QueryResult.Unit.value

import io.bewsys.spmobile.data.repository.CommunityRepositoryImpl
import io.bewsys.spmobile.data.repository.ProvinceRepositoryImpl
import io.bewsys.spmobile.util.PairMediatorLiveData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val provinceRepositoryImpl: ProvinceRepositoryImpl,
    private val communityRepositoryImpl: CommunityRepositoryImpl
) : ViewModel() {

    private val _provinceCount = MutableLiveData(0)
    private val _communityCount = MutableLiveData(0)

    val counts = PairMediatorLiveData(_provinceCount,_communityCount )

    init {
        loadCommunityCount()
        loadProvinceCount()
    }
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


}


