package io.bewsys.spmobile.ui.auth

import androidx.lifecycle.*

class LoginDialogViewModel(
    state: SavedStateHandle
) : ViewModel() {
    var message = state.get<String>("errorMessage") ?: ""
}


