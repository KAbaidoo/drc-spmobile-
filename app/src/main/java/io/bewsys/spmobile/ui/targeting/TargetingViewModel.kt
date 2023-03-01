package io.bewsys.spmobile.ui.targeting


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.bewsys.spmobile.prefsstore.PreferencesManager
import kotlinx.coroutines.launch

class TargetingViewModel(private val preferencesManager: PreferencesManager) : ViewModel() {

 fun logout() = viewModelScope.launch{
        preferencesManager.setLoggedIn(false)
    }



}