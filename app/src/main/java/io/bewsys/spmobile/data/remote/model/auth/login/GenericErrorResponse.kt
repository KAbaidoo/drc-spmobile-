package io.bewsys.spmobile.data.remote.model.auth.login

import kotlinx.serialization.Serializable

@Serializable
data class GenericErrorResponse(
    val errors: Errors?,
    val message: String
)