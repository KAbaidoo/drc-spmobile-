package io.bewsys.spmobile.data.remote.model.auth.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val access_token: String,
    val permissions: List<String>,
    val user: User
)

