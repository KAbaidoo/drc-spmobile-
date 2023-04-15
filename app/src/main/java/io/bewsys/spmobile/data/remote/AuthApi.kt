package io.bewsys.spmobile.data.remote

import io.bewsys.spmobile.data.remote.model.auth.login.LoginRequest
import io.bewsys.spmobile.data.remote.model.auth.password.PasswordRequest
import io.bewsys.spmobile.data.remote.model.profile.UserPayload
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class AuthApi(private val client: HttpClient) {
    suspend fun login(loginRequest: LoginRequest): HttpResponse = client.post("login") {
        setBody(loginRequest)
//        contentType(ContentType.Application.Json)
    }

    suspend fun updateUser(id: Long, payload: UserPayload, accessToken:String): HttpResponse =
        client.patch("user") {
            url { appendPathSegments("$id") }
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
            setBody(payload)
        }

    suspend fun logout(loginRequest: LoginRequest, accessToken:String): HttpResponse = client.post("logout") {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
        setBody(loginRequest)
//        contentType(ContentType.Application.Json)
    }


    suspend fun getPassword(passwordRequest: PasswordRequest): HttpResponse = client.post("forgot-password") {
        setBody(passwordRequest)
//        contentType(ContentType.Application.Json)
    }
}