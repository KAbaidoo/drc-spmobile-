package io.bewsys.spmobile.data.repository

import android.util.Log
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.remote.AuthApi
import io.bewsys.spmobile.data.remote.model.auth.login.ErrorResponse
import io.bewsys.spmobile.data.remote.model.auth.AuthRequest
import io.bewsys.spmobile.data.remote.model.auth.login.LoginResponse
import io.bewsys.spmobile.data.remote.model.auth.logout.LogoutResponse
import io.bewsys.spmobile.data.remote.model.profile.FailureMessage
import io.bewsys.spmobile.data.remote.model.profile.UserPayload
import io.bewsys.spmobile.data.remote.model.profile.UserResponse
import io.bewsys.spmobile.util.Resource
import io.ktor.client.call.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*


private const val TAG = "USER_REPOSITORY"

class AuthRepository(
    private val userApi: AuthApi,
    private val preferencesManager: PreferencesManager
) {

    val isLoggedIn = preferencesManager.preferencesFlow.map {
        it.isLoggedIn
    }

    val userPrefs = preferencesManager.preferencesFlow
    suspend fun setLoggedInState(state: Boolean) {
        preferencesManager.setLoggedIn(state)
    }

    suspend fun login(email: String, password: String) = flow {
        try {
            emit(Resource.Loading)

            val response = userApi.login(AuthRequest(email, password))
            if (response.status.value in 200..299) {

                val res = Resource.Success<LoginResponse>(response.body())
                emit(res)
                preferencesManager.setLoggedIn(true)
                preferencesManager.saveToken(res.data.access_token)
                preferencesManager.saveUser(res.data.user)

            } else {
                emit(Resource.Failure<ErrorResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))

        }
    }.flowOn(Dispatchers.IO)


    suspend fun logOut() = flow {
        try {
            emit(Resource.Loading)
            val userPref =  preferencesManager.preferencesFlow.first()

            userPref.let {
                val response = userApi.logout(AuthRequest(userPref.email, userPref.password),userPref.token)
                if (response.status.value in 200..299) {

                    val res = Resource.Success<LogoutResponse>(response.body())
                    emit(res)
                    preferencesManager.clearUser()


                } else {
                    emit(Resource.Failure<LogoutResponse>(response.body()))
                }
            }

        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))

        }
    }.flowOn(Dispatchers.IO)


    suspend fun updateUser(phoneNumber: String) = flow {

            try {
                emit(Resource.Loading)
               val userPref =  preferencesManager.preferencesFlow.first()

                userPref.id?.let { userPref.id.let { userApi.updateUser(it, UserPayload(phoneNumber), userPref.token) } }
                    ?.let {
                        if (it.status.value in 200..299) {

                            val res = Resource.Success<UserResponse>(it.body())
                            emit(res)
                            preferencesManager.saveUser(res.data.user)

                        } else {
                            emit(Resource.Failure<FailureMessage>(it.body()))
                        }
                    }

            } catch (throwable: Throwable) {
                emit(Resource.Exception(throwable, null))
                Log.d(TAG, "Exception: ${throwable.localizedMessage}")
            }
    }.flowOn(Dispatchers.IO)
}

