package io.bewsys.spmobile.api.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class UserRes(
    @SerialName("id")
    val id: Long,
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String?,
    @SerialName("phone_number")
    val phone_number: String,
    @SerialName("enumerator_team_leader_relation")
    val enumerator_team_leader_relation: TeamLeaderRelation
)
