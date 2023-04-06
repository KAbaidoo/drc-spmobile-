package io.bewsys.spmobile.data.remote.model.auth.login

import kotlinx.serialization.Serializable

@Serializable
data class TeamLeaderSupervisorRelation(
    val created_at: String?,
    val supervisor: Supervisor?,
    val supervisor_id: String?,
    val team_leader_id: Long?,
    val updated_at: String?
)