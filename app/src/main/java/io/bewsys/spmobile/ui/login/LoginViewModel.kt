package io.bewsys.spmobile.ui.login

import androidx.lifecycle.*
import app.cash.sqldelight.db.QueryResult.Unit.value
import io.bewsys.spmobile.data.model.NonConsentHousehold

import io.bewsys.spmobile.data.repository.CommunityRepositoryImpl
import io.bewsys.spmobile.data.repository.ProvinceRepositoryImpl
import io.bewsys.spmobile.data.repository.UserRepository
import io.bewsys.spmobile.data.repository.UserRepositoryImpl
import io.bewsys.spmobile.prefsstore.PreferencesManager
import io.bewsys.spmobile.ui.nonconsenting.form.AddNonConsentingHouseholdViewModel
import io.bewsys.spmobile.util.PairMediatorLiveData
import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel(
    private val state: SavedStateHandle,
    private val userRepository: UserRepositoryImpl,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

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
        userRepository.login(email, password).collectLatest { results ->
            when (results) {
                is Resource.Success -> {
                    _loginEventChannel.send(LoginEvent.LoginSuccessful)
                    preferencesManager.setLoggedIn(true)

                }
                is Resource.Loading -> {
                    _loginEventChannel.send(LoginEvent.Loading)
                }
                else -> {
                    TODO()
                }
            }
        }
    }

    private fun showInvalidInputMessage() = viewModelScope.launch {
        _loginEventChannel.send(
            LoginEvent.ShowInvalidInputMessage(
                "One or more fields empty!"
            )
        )
    }
//


    sealed class LoginEvent {
        data class ShowInvalidInputMessage(val msg: String) : LoginEvent()
        data class NavigateBackWithResults(val results: Int) : LoginEvent()
        object LoginSuccessful : LoginEvent()
        object Loading : LoginEvent()
    }
}


