package io.bewsys.spmobile.ui.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import io.bewsys.spmobile.UPDATE_USER_RESULT_OK
import io.bewsys.spmobile.data.remote.model.login.ErrorResponse
import io.bewsys.spmobile.data.remote.model.profile.ResponseMessage
import io.bewsys.spmobile.data.repository.UserRepository

import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val state: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {



    var phoneNumber = state.get<String>("phone_number") ?: ""
        set(value) {
            field = value
            state["phone_number"] = value
        }

    val userProfile = userRepository.userPrefs

    private val _userProfileEventChannel = Channel<UserProfileEvent>()
    val userProfileEvent = _userProfileEventChannel.receiveAsFlow()

    fun updateButtonClicked() = viewModelScope.launch {


        if (phoneNumber.isBlank()) {
            showInvalidInputMessage()
        } else {
            userRepository.updateUser(phoneNumber).collectLatest { results ->
                when (results) {
                    is Resource.Success -> {
                        _userProfileEventChannel.send(
                            UserProfileEvent.Successful(
                                UPDATE_USER_RESULT_OK
                        ))
                    }
                    is Resource.Loading -> {
                        _userProfileEventChannel.send( UserProfileEvent.Loading)
                    }
                    is Resource.Failure ->{
                        val responseMessage = results.error as ResponseMessage
                        _userProfileEventChannel.send( UserProfileEvent.Failure(responseMessage.msg))
                    }
                    is Resource.Exception -> {
                        results.throwable.localizedMessage?.let { errorMsg->
                            UserProfileEvent.Exception(
                                errorMsg
                            )
                        }?.let { _userProfileEventChannel.send(it) }
                    }
                }
            }
        }
    }
    private fun showInvalidInputMessage() = viewModelScope.launch {
        _userProfileEventChannel.send(
            UserProfileEvent.ShowMessage(
                "One or more fields empty!"
            )
        )
    }



    sealed class UserProfileEvent {
        data class ShowMessage(val msg: String) : UserProfileEvent()
        data class Failure(val errorMsg: String) : UserProfileEvent()
        data class Successful(val results:Int) : UserProfileEvent()

        object Loading : UserProfileEvent()
        data class Exception (val errorMsg:String ): UserProfileEvent()
    }

}