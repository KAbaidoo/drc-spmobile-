package io.bewsys.spmobile.api.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class TeamLeaderRelation(

    @SerialName("team_leader")
    val team_leader: TeamLeader,
    )