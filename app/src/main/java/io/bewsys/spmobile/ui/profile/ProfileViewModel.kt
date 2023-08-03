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
) : ViewModel() {

    private val workManager = WorkManager.getInstance(application)

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
                        val failureMessage = results.error as FailureMessage
                        _userProfileEventChannel.send( UserProfileEvent.Failure(failureMessage.msg))
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


    private fun uploadNonConsentingHousehold(itemId: Long) = viewModelScope.launch {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequestBuilder<NonConsentUploadWorker>()
            .setConstraints(constraints)
            .setInputData(createInputDataForId(itemId))
            .build()
        workManager.enqueue(uploadRequest)

    }
    private fun createInputDataForId(id: Long): Data {
        val builder = Data.Builder()
        builder.putLong(KEY_DATA_ID, id)
        return builder.build()
    }
    sealed class UserProfileEvent {
        data class ShowMessage(val msg: String) : UserProfileEvent()
        data class Failure(val errorMsg: String) : UserProfileEvent()
        data class Successful(val results:Int) : UserProfileEvent()

        object Loading : UserProfileEvent()
        data class Exception (val errorMsg:String ): UserProfileEvent()
    }

}