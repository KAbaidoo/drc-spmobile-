package io.bewsys.spmobile.data.remote

import io.bewsys.spmobile.data.remote.model.auth.AuthRequest
import io.bewsys.spmobile.data.remote.model.profile.UserPayload
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class AuthApi(private val client: HttpClient) {
    suspend fun login(authRequest: AuthRequest): HttpResponse = client.post("login") {
        setBody(authRequest)
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

    suspend fun logout(authRequest: AuthRequest, accessToken:String): HttpResponse = client.post("logout") {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
        setBody(authRequest)
//        contentType(ContentType.Application.Json)
    }
}