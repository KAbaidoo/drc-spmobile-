package io.bewsys.spmobile.data.remote.model.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class NonConsentHousehold(
    val address: String,
    val community_id: String,
    val created_at: String,
    val gps_latitude: String,
    val gps_longitude: String,
    val groupement_id: String,
    val id: Int,
    val other_non_consent_reason: String,
    val province_id: String,
    val reason: String,
    val territory_id: String,
    val updated_at: String,
    val user_id: String
)