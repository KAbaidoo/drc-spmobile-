package io.bewsys.spmobile.data.remote

import io.bewsys.spmobile.data.remote.model.household.HouseholdPayload
import io.bewsys.spmobile.data.remote.model.member.MemberPayload

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


private const val TAG = "MemberApi"

class MemberApi(private val client: HttpClient) {

    suspend fun uploadMember(payload: MemberPayload, accessToken: String): HttpResponse =
        client.post("households/${payload.household_id}/members") {

            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
            setBody(payload)

        }


}



