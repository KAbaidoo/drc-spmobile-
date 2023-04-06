package io.bewsys.spmobile.data.remote.model.auth.logout

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class LogoutResponse(
    @SerialName("message")
    val msg: String,
)