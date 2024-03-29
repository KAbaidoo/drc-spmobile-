package io.bewsys.spmobile.data.remote.model.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPayload(
    @SerialName("phone_number")
    val phoneNumber: String
)
