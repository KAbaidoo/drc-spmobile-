package io.bewsys.spmobile.data.local

import android.os.Parcelable
import io.bewsys.spmobile.data.NonConsentHouseholdEntity
import io.bewsys.spmobile.data.remote.model.noconsent.NonConsentHouseholdPayload
import kotlinx.parcelize.Parcelize


@Parcelize
data class NonConsentHouseholdModel(
    val id: Long? = null,
    val province_id: String?,
    val territory_id: String?,
    val community_id: String?,
    val groupement_id: String?,
    val province_name: String? = null,
    val community_name: String? = null,
    val territory_name: String? = null,
    val groupement_name: String? = null,
    val gps_longitude: String?,
    val gps_latitude: String?,
    val reason: String?,
    val address: String?,
    val other_non_consent_reason: String?,
    val status: String? = null,
) : Parcelable

fun NonConsentHouseholdEntity.toPayload() = NonConsentHouseholdPayload(
    id = this.id,
    province_id = this.province_id,
    community_id = this.community_id,
    territory_id = this.territory_id,
    groupement_id = this.groupement_id,
    address = this.address,
    gps_longitude = this.gps_longitude,
    gps_latitude = this.gps_latitude,
    reason = this.reason,
    other_non_consent_reason = this.other_non_consent_reason,
    status = this.status
)


