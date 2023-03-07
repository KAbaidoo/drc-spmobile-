package io.bewsys.spmobile.data.repository

import android.util.Log
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.remote.UserApi
import io.bewsys.spmobile.data.remote.model.login.ErrorResponse
import io.bewsys.spmobile.data.remote.model.login.LoginRequest
import io.bewsys.spmobile.data.remote.model.login.LoginResponse
import io.bewsys.spmobile.data.remote.model.profile.FailureMessage
import io.bewsys.spmobile.data.remote.model.profile.UserPayload
import io.bewsys.spmobile.data.remote.model.profile.UserResponse
import io.bewsys.spmobile.util.Resource
import io.ktor.client.call.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*


private const val TAG = "USER_REPOSITORY"

class UserRepository(
    private val userApi: UserApi,
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

            val response = userApi.login(LoginRequest(email, password))
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

