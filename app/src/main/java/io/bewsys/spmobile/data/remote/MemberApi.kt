package io.bewsys.spmobile.data.remote

import io.bewsys.spmobile.data.remote.model.household.HouseholdPayload
import io.bewsys.spmobile.data.remote.model.member.MemberPayload

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


private const val TAG = "MemberApi"

class MemberApi(private val client: HttpClient) {

    suspend fun uploadMember(payload: MemberPayload, accessToken: String,householdId:String): HttpResponse =
        client.post("households/$householdId/members") {

            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
            setBody(payload)

        }

    suspend fun updateMember(payload: MemberPayload, accessToken: String): HttpResponse =
        client.post("members/${payload.remote_id}") {

            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
            setBody(payload)

        }


}



