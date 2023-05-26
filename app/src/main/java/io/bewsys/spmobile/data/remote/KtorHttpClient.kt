package io.bewsys.spmobile.data.remote



import android.content.Context
import android.util.Log
import io.bewsys.spmobile.util.getPreferences
import io.ktor.client.*
import io.ktor.client.engine.*

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



class KtorHttpClient(val context: Context) {

    fun getClient() = HttpClient(Android) {

        defaultRequest {
//            url("https://rsu.cd/api/")

             context.getPreferences("primary_host")?.let{
               val baseUrl = it.ifBlank { "https://rsu.cd".trim()}
                url("$baseUrl/api/".trim())
            }


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
//                    longDebug(TAG_KTOR_LOGGER, message)
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
fun longDebug(tag: String, msg: String) {
    val maxLogLength = 4000;
    var msg = msg
    Log.v(tag, msg.substring(0, maxLogLength));
    msg = msg.substring(maxLogLength)
    Log.v(tag, msg);


}