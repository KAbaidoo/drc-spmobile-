package io.bewsys.spmobile.data.remote

import io.bewsys.spmobile.data.remote.model.household.HouseholdPayload

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class HouseholdApi(private val client: HttpClient) {

    suspend fun uploadHousehold(payload: HouseholdPayload, accessToken: String): HttpResponse =
        client.post("households") {

            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
            setBody(payload)

        }

    suspend fun updateHousehold(payload: HouseholdPayload, accessToken: String): HttpResponse =
        client.post("households/${payload.remote_id}/update") {

            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
            setBody(payload)

        }

    companion object {
        private const val TAG = "HouseholdApi"
    }

}



