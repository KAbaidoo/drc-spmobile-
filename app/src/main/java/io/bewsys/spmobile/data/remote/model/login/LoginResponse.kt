package io.bewsys.spmobile.data.remote.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
//    @SerialName("id")
//    val id: Long? = null,
    @SerialName("access_token")
    val access_token: String,
    @SerialName("user")
    val user: UserRes,
    @SerialName("permissions")
    val permissions: List<String>
)
