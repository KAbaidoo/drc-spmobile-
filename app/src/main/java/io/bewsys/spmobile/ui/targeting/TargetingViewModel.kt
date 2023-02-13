package io.bewsys.spmobile.ui.targeting


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val TAG = "TARGETING_VM"
class TargetingViewModel() : ViewModel() {
    fun printAnswer(answer: String) {
        Log.d(TAG, answer)
    }
}