package io.bewsys.spmobile.data.remote.model.auth.login

import kotlinx.serialization.Serializable

@Serializable
data class UserX(
    val api_token: String?,
    val community_id: String? = null,
    val confirmation_code: String?,
    val created_at: String?,
    val deleted_at: String?=null,
    val email: String?,
    val email_verified_at: String? =null,
    val id: Long?,
    val name: String?,
    val password: String?,
    val password_changed_at: String? = null ,
    val phone_number: String,
    val province_id: Long?,
    val remember_token: String? = null,
    val status: String?,
    val ticket_role: Long?,
    val updated_at: String?,
    val vac_type: String?=null
)