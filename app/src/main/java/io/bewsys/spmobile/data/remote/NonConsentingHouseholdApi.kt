package io.bewsys.spmobile.data.remote

import io.bewsys.spmobile.data.remote.model.noconsent.NonConsentHouseholdPayload
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


class NonConsentingHouseholdApi(private val client: HttpClient) {

    suspend fun uploadNonConsentHousehold(payload: NonConsentHouseholdPayload, accessToken:String): HttpResponse =
        client.post  ("households/non_consent") {

            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
            setBody(payload)
        }
}