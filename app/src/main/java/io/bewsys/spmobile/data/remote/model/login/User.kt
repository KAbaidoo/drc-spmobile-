package io.bewsys.spmobile.data.remote.model.login

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val api_token: String?,
    val community_id: String?,
    val confirmation_code: String?,
    val created_at: String?,
    val deleted_at: String?,
    val email: String?,
    val email_verified_at: String?,
    val enumerator_average_survey_time: String?,
    val enumerator_team_leader_relation: EnumeratorTeamLeaderRelation?,
    val enumerator_total_survey_time: String?,
    val enumerator_total_survey_time_seconds: Long?,
    val households_surveyed: List<HouseholdsSurveyed>?,
    val id: Long?,
    val name: String?,
    val password: String?,
    val password_changed_at: String?,
    val phone_number: String?,
    val province_id: String?,
    val remember_token: String?,
    val role: Role?,
    val status: String?,
    val supervisor: String?,
    val team_leader: String?,
    val team_leader_supervisor_relation: String?,
    val ticket_role: String?,
    val updated_at: String?,
    val vac_type: String?
)