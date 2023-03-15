package io.bewsys.spmobile.data.remote

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class DashboardApi(private val client: HttpClient) {
    suspend fun fetchData(accessToken:String): HttpResponse = client.get("dashboard") {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }

}