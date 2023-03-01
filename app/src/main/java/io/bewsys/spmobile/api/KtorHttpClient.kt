package io.bewsys.spmobile.api


import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.json.Json


class KtorHttpClient(private val httpClientEngine: HttpClientEngine) {

    fun getClient() = HttpClient(httpClientEngine) {

        defaultRequest {
            url("http://mis-portal.makedudev.com/api/")

            headers.appendIfNameAbsent(HttpHeaders.ContentType,
                ContentType.Application.Json.toString()
            )
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }


//        install(Logging)
//        {
//            logger = object : Logger {
//                override fun log(message: String) {
//                    Log.v("Logger Ktor =>", message)
//                }
//            }
//            level = LogLevel.ALL
//        }

//        install(ResponseObserver)
//        {
//            onResponse { response ->
//                Log.d("HTTP status:", "${response.status.value}")
//            }
//        }

        install(DefaultRequest)
        {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }


    }

//    companion object {
//        private const val TIME_OUT = 10_000
//        private const val TAG_KTOR_LOGGER = "ktor_logger:"
//        private const val TAG_HTTP_STATUS_LOGGER = "http_status:"
//
//    }

}
