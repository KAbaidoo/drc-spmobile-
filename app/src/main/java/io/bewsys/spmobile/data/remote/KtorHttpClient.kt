package io.bewsys.spmobile.data.remote



import android.util.Log
import io.ktor.client.*

import io.ktor.client.engine.android.*

import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*

import io.ktor.client.request.*

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.json.Json



class KtorHttpClient{

    fun getClient() = HttpClient(Android) {

        defaultRequest {
            url("http://mis.bewsys.dev/api/")

            headers.appendIfNameAbsent(
                HttpHeaders.ContentType,
                ContentType.Application.Json.toString()
            )
            accept(ContentType.Application.Json)

        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }


        install(Logging)
        {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v(TAG_KTOR_LOGGER, message)
                }
            }
            level = LogLevel.ALL
        }

        install(ResponseObserver)
        {
            onResponse { response ->
                Log.d(TAG_HTTP_STATUS_LOGGER, "${response.status.value}")
            }
        }

//        engine {
//            connectTimeout = 120_000
//            socketTimeout =120_000
//
//        }

//        install(DefaultRequest)
//        {
//            header(HttpHeaders.ContentType, ContentType.Application.Json)
//
//        }
        install(HttpTimeout) {
            connectTimeoutMillis = TIME_OUT
            socketTimeoutMillis = TIME_OUT
            requestTimeoutMillis = TIME_OUT
        }


    }

    companion object {
        private const val TIME_OUT = 60_000L
        private const val TAG_KTOR_LOGGER = "ktor_logger:"
        private const val TAG_HTTP_STATUS_LOGGER = "http_status:"
    }

}
