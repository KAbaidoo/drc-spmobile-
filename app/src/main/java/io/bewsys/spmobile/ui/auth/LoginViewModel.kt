package io.bewsys.spmobile.ui.auth

import androidx.lifecycle.*
import io.bewsys.spmobile.LOGIN_RESULT_OK
import io.bewsys.spmobile.data.remote.model.auth.login.ErrorResponse
import io.bewsys.spmobile.data.remote.model.auth.logout.LogoutResponse

import io.bewsys.spmobile.data.repository.AuthRepository
import io.bewsys.spmobile.ui.common.BaseViewModel
import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val TAG = "LoginVM"

class LoginViewModel(
    private val state: SavedStateHandle,
    private val authRepository: AuthRepository
) : BaseViewModel<Unit>() {

    init {
        setLoggedInState()
    }


    private fun setLoggedInState() = viewModelScope.launch {
        authRepository.setLoggedInState(false)
    }




    var email = state.get<String>("email") ?: ""
        set(value) {
            field = value
            state["email"] = value
        }

    var password = state.get<String>("password") ?: ""
        set(value) {
            field = value
            state["password"] = value
        }


    fun loginClicked() = viewModelScope.launch {
        if (email.isBlank() || password.isBlank()) {
            showInvalidInputMessage()
        } else {
            authRepository.login(email, password).collectLatest { results ->
                when (results) {
                    is Resource.Success -> {
                        _eventChannel.send(
                            Event.Successful(
                                LOGIN_RESULT_OK
                            )
                        )
                    }
                    is Resource.Loading -> {
                        _eventChannel.send(Event.Loading)
                    }
                    is Resource.Failure -> {
                        _eventChannel.send(Event.Error(results.error.toString()))
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

    fun logout() = viewModelScope.launch {

        authRepository.logOut().collectLatest { results ->
            when (results) {
                is Resource.Success -> {
                    val response = results.data as LogoutResponse
                    _eventChannel.send(
                        Event.ShowSnackBar(
                            response.msg
                        )
                    )
                }
                is Resource.Loading -> {
                    _eventChannel.send(Event.Loading)
                }
                is Resource.Failure -> {
                    val response = results.error as LogoutResponse
                    _eventChannel.send(Event.Error(response.msg))
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

    private fun showInvalidInputMessage() = viewModelScope.launch {
        _eventChannel.send(
            Event.ShowSnackBar(
                "One or more fields empty!"
            )
        )
    }



    fun showLoggedOutSnackMessage() {
        viewModelScope.launch {
            delay(400L)
            _eventChannel.send(
                Event.ShowSnackBar(
                    "You logged out!"
                )
            )
        }
    }



}


