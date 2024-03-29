package io.bewsys.spmobile.ui.auth

import androidx.lifecycle.*
import io.bewsys.spmobile.FORGOT_PASSWORD_RESULT_OK
import io.bewsys.spmobile.data.remote.model.auth.login.ErrorResponse
import io.bewsys.spmobile.data.repository.AuthRepository
import io.bewsys.spmobile.ui.common.BaseViewModel

import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val state: SavedStateHandle,
    private val repository: AuthRepository
) : BaseViewModel<Unit>() {


    var email = state.get<String>("email") ?: ""
        set(value) {
            field = value
            state["email"] = value
        }

    fun btnGetPasswordClicked() = viewModelScope.launch {

        repository.getPasswordLink(email).collectLatest { results ->
            when (results) {
                is Resource.Success -> {
                    _eventChannel.send(
                        Event.Successful(
                            FORGOT_PASSWORD_RESULT_OK
                        )
                    )
                }

                is Resource.Loading -> {
                    _eventChannel.send(Event.Loading)
                }

                is Resource.Failure -> {
                    val errorResponse = results.error as ErrorResponse
                    _eventChannel.send(Event.Error(errorResponse.msg))
                }

                is Resource.Exception -> {
                    results.throwable.localizedMessage?.let { errorMsg ->
                        Event.Error(
                            errorMsg
                        )
                    }?.let { _eventChannel.send(it) }
                }
            }

        }
    }







}


