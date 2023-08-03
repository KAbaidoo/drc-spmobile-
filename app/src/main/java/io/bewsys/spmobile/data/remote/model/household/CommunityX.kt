package io.bewsys.spmobile.data.remote.model.household

import kotlinx.serialization.Serializable

@Serializable
data class CommunityX(
    val created_at: String?,
    val id: Long?,
    val name: String?,
    val survey_no_code: String?,
    val system_default: String?,
    val territory_id: String?,
    val updated_at: String?
)