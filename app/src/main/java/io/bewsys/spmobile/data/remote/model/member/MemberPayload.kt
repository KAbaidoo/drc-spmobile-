package io.bewsys.spmobile.data.remote.model.member

import kotlinx.serialization.Serializable


@Serializable
data class MemberPayload(
    val id: Long?=null,
    val remote_id: String?=null,
    val firstname: String?=null,
    val middlename: String?=null,
    val lastname: String?=null,
    val sex: String?=null,
    val age: String?=null,
    val dob_known: String?=null,
    val age_known: String?=null,
    val date_of_birth: String?=null,
    val profile_picture: String?=null,
    val is_head: Int?=null,
    val is_member_respondent: Int?=null,
    val family_bond_id: Int? =null,
    val marital_status_id: Int?=null,
    val birth_certificate: Int? =null,
    val educational_level_id: Int? =null,
    val school_attendance_id: Int? =null,
    val pregnancy_status: Int? =null,
    val disability_id: Int? =null,
    val socio_professional_category_id: Int? =null,
    val sector_of_work_id: Int? =null,
    val household_id: String?=null
)