package io.bewsys.spmobile.data.remote

import android.util.Log
import io.bewsys.spmobile.data.remote.model.household.HouseholdPayload

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.PartData


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






   suspend fun uploadHouseholdBulk(formdata: MultiPartFormDataContent, accessToken: String): HttpResponse =
        client.post("households/bulk") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
            contentType(ContentType.MultiPart.FormData)
            setBody(
               formdata
            )

        }

    companion object {
        private const val TAG = "HouseholdApi"
    }

}



