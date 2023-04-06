package io.bewsys.spmobile.data.remote.model.auth.login

import kotlinx.serialization.Serializable

@Serializable
data class EnumeratorTeamLeaderRelation(
    val created_at: String?,
    val enumerator_id: Long?,
    val team_leader: TeamLeader?,
    val team_leader_id: String?,
    val updated_at: String?
)