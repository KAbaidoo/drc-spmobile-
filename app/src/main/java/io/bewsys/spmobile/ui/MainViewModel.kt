package io.bewsys.spmobile.ui

import androidx.lifecycle.ViewModel
import io.bewsys.spmobile.data.prefsstore.UserPreferences
import io.bewsys.spmobile.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take

class MainViewModel(private val userRepository: AuthRepository): ViewModel() {
    val userState = userRepository.isLoggedIn

    fun getUser(): Flow<UserPreferences> = userRepository.userPrefs

}