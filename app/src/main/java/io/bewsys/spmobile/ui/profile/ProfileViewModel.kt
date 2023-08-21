package io.bewsys.spmobile.ui.profile

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import io.bewsys.spmobile.KEY_DATA_ID

import io.bewsys.spmobile.UPDATE_USER_RESULT_OK
import io.bewsys.spmobile.data.remote.model.profile.FailureMessage
import io.bewsys.spmobile.data.repository.AuthRepository
import io.bewsys.spmobile.ui.common.BaseViewModel

import io.bewsys.spmobile.util.Resource
import io.bewsys.spmobile.work.NonConsentUploadWorker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application,
    private val state: SavedStateHandle,
    private val userRepository: AuthRepository
) : BaseViewModel<Unit>() {

    private val workManager = WorkManager.getInstance(application)

    var phoneNumber = state.get<String>("phone_number") ?: ""
        set(value) {
            field = value
            state["phone_number"] = value
        }

    val userProfile = userRepository.userPrefs


    fun updateButtonClicked() = viewModelScope.launch {


        if (phoneNumber.isBlank()) {
            showInvalidInputMessage()
        } else {
            userRepository.updateUser(phoneNumber).collectLatest { results ->
                when (results) {
                    is Resource.Success -> {
                        _eventChannel.send(
                            Event.Successful(
                                UPDATE_USER_RESULT_OK
                        ))
                    }
                    is Resource.Loading -> {
                        _eventChannel.send( Event.Loading)
                    }
                    is Resource.Failure ->{
                        val failureMessage = results.error as FailureMessage
                        _eventChannel.send( Event.Error(failureMessage.msg))
                    }
                    is Resource.Exception -> {
                        results.throwable.localizedMessage?.let { errorMsg->
                            Event.Error(
                                errorMsg
                            )
                        }?.let { _eventChannel.send(it) }
                    }
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


}