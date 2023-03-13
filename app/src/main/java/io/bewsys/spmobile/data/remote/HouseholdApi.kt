package io.bewsys.spmobile.data.remote

import android.util.Log
import io.bewsys.spmobile.data.remote.model.household.HouseholdPayload

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


const val TAG = "HouseholdApi"

class HouseholdApi(private val client: HttpClient) {

    suspend fun uploadHousehold(payload: HouseholdPayload, accessToken: String): HttpResponse =
        client.post("households") {

            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
            setBody(payload)

        }


}



