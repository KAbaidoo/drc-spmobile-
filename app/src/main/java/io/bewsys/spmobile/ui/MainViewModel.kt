package io.bewsys.spmobile.ui

import androidx.lifecycle.ViewModel
import io.bewsys.spmobile.data.repository.AuthRepository

class MainViewModel(private val userRepository: AuthRepository): ViewModel() {
    val userState = userRepository.isLoggedIn
}