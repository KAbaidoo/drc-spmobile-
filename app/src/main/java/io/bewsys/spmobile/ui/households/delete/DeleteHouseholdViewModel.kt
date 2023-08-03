package io.bewsys.spmobile.ui.households.delete

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.util.ApplicationScope
import io.bewsys.spmobile.util.provideApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteHouseholdViewModel (
    val state:SavedStateHandle,
    private val repository:HouseholdRepository,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {
    val id:Long? = state["id"]
     fun onConfirmDeleteHouseholdClicked()= applicationScope.launch {
         if (id != null) {
             repository.deleteHousehold(id)
         }
    }
}