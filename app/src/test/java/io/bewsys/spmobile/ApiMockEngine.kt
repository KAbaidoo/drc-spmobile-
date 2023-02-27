package io.bewsys.spmobile

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*

class ApiMockEngine {
    fun get() = client.engine

    private val responseHeaders =
        headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
    private val client = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                when (request.url.encodedPath) {
                    "/login" -> respond(
                        MockResponse.LoginMockResponse(),
                        HttpStatusCode.OK,
                        responseHeaders
                    )

                    else -> {
                        error("Unhandled ${request.url.encodedPath}")
                    }
                }
            }
        }

    }

    sealed class MockResponse {
        object LoginMockResponse : MockResponse() {
            operator fun invoke(): String = """{"message":"Login Successful!"}"""
        }
    }
}
