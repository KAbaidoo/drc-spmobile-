package io.bewsys.spmobile

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.util.*

class ApiMockEngine {
    fun get() = client.engine

    private val responseHeaders =
        headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
    private val client = HttpClient(MockEngine) {

        engine {

            addHandler { request ->
                when (request.url.encodedPath) {
                    "/api/login" -> {
                        respond(
                            MockResponse.LoginMockResponse(), HttpStatusCode.OK, responseHeaders
                        )
                    }
                    else -> {
                        error("Unhandled ${request.url.encodedPath}")
                    }
                }
            }
        }

    }

    sealed class MockResponse {
        object LoginMockResponse : MockResponse() {
            operator fun invoke(): String = """{
  "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVc2VyIERldGFpbHMiLCJpc3MiOiJzcHJpbmctc2VjdXJpdHktand0IiwiaWF0IjoxNjcwMzM2NjkxLCJlbWFpbCI6InVzZXIub25lQGVtYWlsLmNvbSJ9.k_Df_gk4K2gk5KDcpZrcZ3rZ2AaG3IN2xc8Jsoa7lUQ",
  "user": {
    "name": "Kobby",
    "id": 1,
    "email": "kobbykolmes@gmail.com",
    "phone_number": "0555656645",
    "enumerator_team_leader_relation": {
      "team_leader": {
        "supervisor": "supervisor one",
        "id": "supervisor one",
        "name": "supervisor one"
      }
    }
  },
  "permissions": [
    "admin",
    "enumerator",
    "user"
  ]
}"""
        }
    }
}
