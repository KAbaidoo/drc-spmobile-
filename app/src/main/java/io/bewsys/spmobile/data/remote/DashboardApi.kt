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

    suspend fun fetchProvinceData(accessToken:String): HttpResponse = client.get("dashboard/province") {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }
    suspend fun fetchTerritoriesData(accessToken:String): HttpResponse = client.get("dashboard/territories") {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }
    suspend fun fetchCommunitiesData(accessToken:String): HttpResponse = client.get("dashboard/communities") {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }
    suspend fun fetchGroupmentsData(accessToken:String): HttpResponse = client.get("dashboard/groupments") {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }
    suspend fun fetchHealthZonesData(accessToken:String): HttpResponse = client.get("dashboard/health-zones") {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }
    suspend fun fetchHealthAreasData(accessToken:String): HttpResponse = client.get("dashboard/health-areas") {
        headers {
            append(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }

}

/*
/api/dashboard/province
/api/dashboard/territories
/api/dashboard/communities
/api/dashboard/targets
/api/dashboard/non-consent-households
/api/dashboard/groupments
/api/dashboard/cbt-area-assignments
/api/dashboard/forms
/api/dashboard/health-zones
/api/dashboard/health-areas
/api/dashboard/permissions*/
