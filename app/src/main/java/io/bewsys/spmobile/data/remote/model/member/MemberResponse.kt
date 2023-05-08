package io.bewsys.spmobile.data.remote.model.member

import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    val member: Member?
)