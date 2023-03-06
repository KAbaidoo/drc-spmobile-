package io.bewsys.spmobile.data.remote.model.profile

import io.bewsys.spmobile.data.remote.model.login.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val user: User
)