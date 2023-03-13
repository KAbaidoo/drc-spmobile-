package io.bewsys.spmobile.ui.login

import android.util.Log
import androidx.lifecycle.*
import io.bewsys.spmobile.LOGIN_RESULT_OK
import io.bewsys.spmobile.data.remote.model.login.ErrorResponse
import io.bewsys.spmobile.data.remote.model.login.LoginResponse

import io.bewsys.spmobile.data.repository.UserRepository
import io.bewsys.spmobile.util.Resource
import io.ktor.client.call.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginDialogViewModel(
    state: SavedStateHandle
) : ViewModel() {
    var message = state.get<String>("errorMessage") ?: ""
}


