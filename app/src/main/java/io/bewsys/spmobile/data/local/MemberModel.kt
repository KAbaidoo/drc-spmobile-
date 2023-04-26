package io.bewsys.spmobile.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemberModel(
    val id: Long?=null,
    val remote_id: String?=null,
    val firstname: String?=null,
    val middlename: String?=null,
    val lastname: String?=null,

    val sex: String?=null,
    val age: String?=null,
    val dob: String?=null,
    val age_known: String?=null,
    val dob_known: String?=null,
    val profile_picture: String?=null,
    val is_head: String?=null,
    val is_member_respondent: String?=null,
    val family_bond_id: String?=null,
    val marital_status_id: String?=null,
    val birth_certificate: String?=null,
    val educational_level_id: String?=null,
    val school_attendance_id: String?=null,
    val pregnancy_status: String?=null,
    val disability_id: String?=null,
    val socio_professional_category_id: String?=null,
    val sector_of_work_id: String?=null,
    val household_id: String?=null,
    val status: String?=null,
) : Parcelable

