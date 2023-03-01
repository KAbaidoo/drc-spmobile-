package io.bewsys.spmobile.api

import io.bewsys.spmobile.api.model.login.LoginRequest
import io.bewsys.spmobile.api.model.login.LoginResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class UserApi(private val client: HttpClient) {

    suspend fun login(email: String, password: String): HttpResponse = client.post("login") {
        setBody(LoginRequest(email, password))
//        contentType(ContentType.Application.Json)
    }
}