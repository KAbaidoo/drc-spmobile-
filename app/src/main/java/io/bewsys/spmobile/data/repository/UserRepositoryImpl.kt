package io.bewsys.spmobile.data.repository

import io.bewsys.spmobile.api.UserApi
import io.bewsys.spmobile.api.model.login.LoginResponse
import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class UserRepositoryImpl(private val userApi: UserApi) {


    suspend fun login(email: String, password: String) = flow {
        emit(Resource.Loading)
        try {

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
//            val response = userApi.login(email, password)
//
//            if (response.status.value in 200..299) {
//
////                emit(Resource.Success<LoginResponse>(response.body()))
//            } else {
//                TODO("Handle failure")
//            }

        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }

    }.flowOn(Dispatchers.IO)
}