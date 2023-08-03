package io.bewsys.spmobile.data.remote.model.dashboard

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class HealthZone(
    val id: Long,
    val name: String,
    val province_id: String,


    @Transient
    val updated_at: String?=null,
    @Transient
    val created_at:  String?=null
)