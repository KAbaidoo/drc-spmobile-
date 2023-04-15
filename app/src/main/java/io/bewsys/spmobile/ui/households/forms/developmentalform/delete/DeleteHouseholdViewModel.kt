package io.bewsys.spmobile.ui.households.forms.developmentalform.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.bewsys.spmobile.data.repository.HouseholdRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteHouseholdViewModel (
    private val repository:HouseholdRepository
) : ViewModel() {
     fun onConfirmDeleteHouseholdClicked(id: Long)= viewModelScope.launch {
        repository.deleteHousehold(id)
//         onConfirmDeleteHouseholdClicked()

    }
}