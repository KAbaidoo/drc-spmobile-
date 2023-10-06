package io.bewsys.spmobile.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.bewsys.spmobile.ui.auth.LoginViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<UIState : Any?> : ViewModel() {

    protected val _eventChannel = Channel<Event>()
    open val events = _eventChannel.receiveAsFlow()

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState>
        get() = _uiState


   sealed class Event {
        data class Successful(val result: Int) : Event()
        data class Error(val errorMsg: String) : Event()
        data class ShowSnackBar(val msg: String) : Event()
        object Loading : Event()
    }

}

