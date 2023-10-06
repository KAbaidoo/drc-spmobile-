package io.bewsys.spmobile.ui.households.detail


import android.util.Log
import androidx.lifecycle.*

import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.ui.common.BaseFragment
import io.bewsys.spmobile.ui.common.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class HouseholdDetailViewModel(
) : BaseViewModel<Unit>() {
    var household: HouseholdModel? = null
    var id: Long? = null
}