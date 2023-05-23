package io.bewsys.spmobile.data.remote.model.dashboard

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
data class HealthArea(
    val id: Long,
    val name: String,
    val health_zone_id: String,

    @Transient
    val updated_at: String? = null,
    @Transient
    val created_at: String? = null
)