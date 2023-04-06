package io.bewsys.spmobile.data.remote.model.auth.login

import kotlinx.serialization.Serializable

@Serializable
data class Role(
    val display_name: String?,
    val id: Long?,
    val name: String?,
    val system_default: String?
)