package io.bewsys.spmobile.ui

import androidx.lifecycle.ViewModel
import io.bewsys.spmobile.data.repository.UserRepository

class MainViewModel(private val userRepository: UserRepository): ViewModel() {
    val userState = userRepository.isLoggedIn
}