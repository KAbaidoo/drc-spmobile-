package io.bewsys.spmobile.data.remote.model.household

import kotlinx.serialization.Serializable

@Serializable
data class Head(
    val age: String?,
    val age_known: String?,
    val birth_certificate: String?,
    val comments: String?,
    val created_at: String?,
    val date_of_birth: String?,
    val deleted_at: String?,
    val disability_id: String?,
    val dob_known: String?,
    val educational_level_id: String?,
    val family_bond_id: String?,
    val firstname: String?,
    val household_id: String?,
    val id: Long,
    val is_head: String?,
    val is_member_respondent: String?,
    val lastname: String?,
    val marital_status_id: String?,
    val middlename: String?,
    val other_sector_of_work: String?,
    val other_socio_professional_category: String?,
    val pregnancy_status: String?,
    val profile_picture: String?,
    val profile_picture_url: String?,
    val school_attendance_id: String,
    val sector_of_work_id: String?,
    val sex: String?,
    val socio_professional_category_id: String?,
    val updated_at: String?
)