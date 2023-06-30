package io.bewsys.spmobile.data.remote.model.auth.login

import kotlinx.serialization.Serializable

@Serializable
data class Errors(
    val email: List<String>? =null,
    val password: List<String>?=null
)