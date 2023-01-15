package io.bewsys.spmobile.ui.households

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class HouseholdsViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle) : ViewModel() {
}