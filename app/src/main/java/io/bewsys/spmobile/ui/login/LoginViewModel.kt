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
const val TAG = "LOGIN_VIEW_MODEL"
class LoginViewModel(
    private val state: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    init {
        setLoggedInState()
    }
    private fun setLoggedInState()= viewModelScope.launch {
        userRepository.setLoggedInState(false)
    }

    private val _loginEventChannel = Channel<LoginEvent>()
    val loginEvent = _loginEventChannel.receiveAsFlow()

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


    fun login() = viewModelScope.launch {
        if (email.isBlank() || password.isBlank()) {
            showInvalidInputMessage()
        } else {
            userRepository.login(email, password).collectLatest { results ->
                when (results) {
                    is Resource.Success -> {
                        _loginEventChannel.send(LoginEvent.Successful(
                            LOGIN_RESULT_OK
                        ))
                    }
                    is Resource.Loading -> {
                        _loginEventChannel.send(LoginEvent.Loading)
                    }
                    is Resource.Failure ->{
                        val errorResponse = results.error as ErrorResponse
                        _loginEventChannel.send(LoginEvent.Failure(errorResponse.msg))
                    }
                    is Resource.Exception -> {
                        results.throwable.localizedMessage?.let { errorMsg->
                            LoginEvent.Exception(
                                errorMsg
                            )
                        }?.let { _loginEventChannel.send(it) }
                    }
                }
            }
        }
    }

    private fun showInvalidInputMessage() = viewModelScope.launch {
        _loginEventChannel.send(
            LoginEvent.ShowMessage(
                "One or more fields empty!"
            )
        )
    }

    fun showLoggedOutMessage()= viewModelScope.launch{
        delay(400L)
        _loginEventChannel.send(
            LoginEvent.ShowMessage(
                "You logged out!"
            )
        )
    }

    sealed class LoginEvent {
        data class ShowMessage(val msg: String) : LoginEvent()
        data class Failure(val errorMsg: String) : LoginEvent()
        data class Successful(val results:Int) : LoginEvent()

        object Loading : LoginEvent()
        data class Exception (val errorMsg:String ): LoginEvent()
    }
}


