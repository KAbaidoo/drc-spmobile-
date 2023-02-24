package io.bewsys.spmobile.ui.households.forms

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DevelopmentalFormViewModel : ViewModel() {
    fun printResults(title:String, str: String) {
        viewModelScope.launch {
            Log.d("FORM_VIEW_MODEL", "$title: $str")
        }
    }
}