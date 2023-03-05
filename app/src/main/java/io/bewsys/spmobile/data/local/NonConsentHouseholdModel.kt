package io.bewsys.spmobile.data.local
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class NonConsentHouseholdModel(
    val id: Long? = null,
    val province_id: Long?,
    val community_id: Long?,
    val province_name: String?=null,
    val community_name: String? =null,
    val gps_longitude: String?,
    val gps_latitude: String?,
    val reason: String?,
    val other_non_consent_reason: String?,
    val status: String? = null,
):Parcelable

