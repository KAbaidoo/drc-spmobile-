package io.bewsys.spmobile.api.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("permissions")
    val password: String
)
