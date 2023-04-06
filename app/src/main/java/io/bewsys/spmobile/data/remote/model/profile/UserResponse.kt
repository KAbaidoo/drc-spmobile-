package io.bewsys.spmobile.data.remote.model.profile

import io.bewsys.spmobile.data.remote.model.auth.login.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val user: User
)