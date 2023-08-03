package io.bewsys.spmobile.data.remote.model.household

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val local: Int?,
    val remote_id: Int?
)