package io.bewsys.spmobile.data.remote.model.auth.password

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PasswordResponse(
    @SerialName("status")
    val status: String
)
