package io.bewsys.spmobile.data.remote.model.auth.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class ErrorResponse(
    @SerialName("error")
    val msg: String,
)