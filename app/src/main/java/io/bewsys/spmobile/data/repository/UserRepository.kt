package io.bewsys.spmobile.data.repository

import androidx.lifecycle.asLiveData
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.remote.UserApi
import io.bewsys.spmobile.data.remote.model.login.ErrorResponse
import io.bewsys.spmobile.data.remote.model.login.LoginResponse
import io.bewsys.spmobile.util.Resource
import io.ktor.client.call.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class UserRepository(
    private val userApi: UserApi,
    private val preferencesManager: PreferencesManager
) {

     val isLoggedIn = preferencesManager.preferencesFlow.map {
            it.isLoggedIn
        }

    suspend fun setLoggedInState(state:Boolean){
        preferencesManager.setLoggedIn(state)
    }


    suspend fun login(email: String, password: String) = flow {

        try {
            emit(Resource.Loading)
            val loginResponse = Json.decodeFromString<LoginResponse>(
                """{
    "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VyIERldGFpbHMiLCJpc3MiOiJzcHJpbmctc2VjdXJpdHktand0IiwiaWF0IjoxNjcwMzM2NjkxLCJlbWFpbCI6InVzZXIub25lQGVtYWlsLmNvbSJ9.k_Df_gk4K2gk5KDcpZrcZ3rZ2AaG3IN2xc8Jsoa7lUQ",
    "user": {
      "name": "Kobby",
      "id": 1,
      "email": "kobbykolmes@gmail.com",
      "phone_number": "0555656645",
      "enumerator_team_leader_relation": {
        "team_leader": {
          "supervisor": "supervisor one",
          "id": "supervisor one",
          "name": "supervisor one"
        }
      }
    },
    "permissions": [
      "admin",
      "enumerator",
      "user"
    ]
  }"""
            )
            emit(Resource.Success<LoginResponse>(loginResponse))
            preferencesManager.setLoggedIn(true)

//            val response = userApi.login(email, password)
//            if (response.status.value in 200..299) {
//                emit(Resource.Success<LoginResponse>(response.body()))
//                preferencesManager.setLoggedIn(true)
//            }else {
//                emit(Resource.Failure<ErrorResponse>(response.body()))
//            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)
}

