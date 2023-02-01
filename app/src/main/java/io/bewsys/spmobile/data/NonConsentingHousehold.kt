package io.bewsys.spmobile.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NonConsentingHousehold(
    val reason: String,
    val otherReason: String,
    val province: String,
    val territory: String,
    val community: String,
    val groupment: String,
    val address: String,
    val lon: String,
    val lat: String,
) : Parcelable

