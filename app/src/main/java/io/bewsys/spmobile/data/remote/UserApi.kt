package io.bewsys.spmobile.data.remote

import io.bewsys.spmobile.data.remote.model.login.LoginRequest
import io.bewsys.spmobile.data.remote.model.profile.UserPayload
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class UserApi(private val client: HttpClient) {
    suspend fun login(loginRequest: LoginRequest): HttpResponse = client.post("login") {
        setBody(loginRequest)
//        contentType(ContentType.Application.Json)
    }

    suspend fun updateUser(id: Long, userProfile: UserPayload, accessToken:String): HttpResponse =
        client.patch("user") {
            url { appendPathSegments("$id") }
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
            setBody(userProfile)
        }
}