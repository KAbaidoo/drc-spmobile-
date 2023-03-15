package io.bewsys.spmobile.data.remote.model.noconsent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NonConsentHouseholdPayload(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("province_id")
    val province_id: String?,
    @SerialName("community_id")
    val community_id: String?,
    @SerialName("territory_id")
    val territory_id: String?,
    @SerialName("groupement_id")
    val groupement_id: String?,
    @SerialName("address")
    val address: String?,
    @SerialName("gps_longitude")
    val gps_longitude: String?,
    @SerialName("gps_latitude")
    val gps_latitude: String?,
    @SerialName("reason")
    val reason: String?,
    @SerialName("other_non_consent_reason")
    val other_non_consent_reason: String?,
    @SerialName("status")
    val status: String? = null,
)

