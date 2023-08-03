package io.bewsys.spmobile.ui.auth

import androidx.lifecycle.*
import io.bewsys.spmobile.LOGIN_RESULT_OK
import io.bewsys.spmobile.data.remote.model.auth.login.ErrorResponse
import io.bewsys.spmobile.data.remote.model.auth.password.PasswordResponse
import io.bewsys.spmobile.data.repository.AuthRepository
import io.bewsys.spmobile.data.repository.HouseholdRepository

import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val state: SavedStateHandle,
    private val repository: AuthRepository
) : ViewModel() {


    private val _PasswordEventChannel = Channel<ForgotPassword>()
    val PasswordEventChannel = _PasswordEventChannel.receiveAsFlow()

    var email = state.get<String>("email") ?: ""
        set(value) {
            field = value
            state["email"] = value
        }

    fun btnGetPasswordClicked() = viewModelScope.launch {

        repository.getPasswordLink(email).collectLatest { results ->
            when (results) {
                is Resource.Success -> {
                    val msg = results.data as PasswordResponse
                    _PasswordEventChannel.send(ForgotPassword.Successful(msg.status))
                }
                is Resource.Loading -> {
                    _PasswordEventChannel.send(ForgotPassword.Loading)
                }
                is Resource.Failure -> {
                    val errorResponse = results.error as ErrorResponse
                    _PasswordEventChannel.send(ForgotPassword.Failure(errorResponse.msg))
                }
                is Resource.Exception -> {
                    results.throwable.localizedMessage?.let { errorMsg ->
                        ForgotPassword.Exception(
                            errorMsg
                        )
                    }?.let { _PasswordEventChannel.send(it) }
                }
            }

        }
    }


    fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        _PasswordEventChannel.send(
            ForgotPassword.ShowSnackMessage(msg)
        )
    }

    fun goToLoginClicked()= viewModelScope.launch {
        _PasswordEventChannel.send(
            ForgotPassword.NavigateToLoginFragment(email)
        )
    }


    sealed class ForgotPassword {
        data class ShowSnackMessage(val msg: String) : ForgotPassword()
        data class Failure(val errorMsg: String) : ForgotPassword()
        data class Successful(val msg: String) : ForgotPassword()
        data class NavigateToLoginFragment(val email: String) : ForgotPassword()
        object Loading : ForgotPassword()
        data class Exception(val errorMsg: String) : ForgotPassword()
    }
}


