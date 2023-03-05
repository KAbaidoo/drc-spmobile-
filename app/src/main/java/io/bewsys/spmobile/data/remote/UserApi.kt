package io.bewsys.spmobile.data.remote

import io.bewsys.spmobile.data.remote.model.login.LoginRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*


class UserApi(private val client: HttpClient) {
    suspend fun login(email: String, password: String): HttpResponse = client.post("login") {
        setBody(LoginRequest(email, password))
//        contentType(ContentType.Application.Json)
    }
}