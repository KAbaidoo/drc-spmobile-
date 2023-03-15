package io.bewsys.spmobile.data.remote.model.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class Province(
    val id: Long,
    val name: String,
    val survey_no_code: String,

    @Transient
    val created_at: String?=null,
    @Transient
    val updated_at: String?=null,
    @Transient
    val system_default: String?=null
)