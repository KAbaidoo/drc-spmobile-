package io.bewsys.spmobile.data.local

import android.os.Parcelable
import io.bewsys.spmobile.data.MemberEntity
import io.bewsys.spmobile.data.remote.model.member.MemberPayload
import io.bewsys.spmobile.util.MapUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemberModel(
    val id: Long? = null,
    val remote_id: String? = null,
    val firstname: String? = null,
    val middlename: String? = null,
    val lastname: String? = null,
    val sex: String? = null,
    val age: String? = null,
    val dob: String? = null,
    val age_known: String? = null,
    val dob_known: String? = null,
    val profile_picture: String? = null,
    val is_head: String? = null,
    val is_member_respondent: String? = null,
    val family_bond_id: String? = null,
    val marital_status_id: String? = null,
    val birth_certificate: String? = null,
    val educational_level_id: String? = null,
    val school_attendance_id: String? = null,
    val pregnancy_status: String? = null,
    val disability_id: String? = null,
    val socio_professional_category_id: String? = null,
    val sector_of_work_id: String? = null,
    var household_id: String? = null,
    val status: String? = null,
) : Parcelable

fun MemberEntity.toPayLoad() = MemberPayload(
    id = this.id,
    remote_id = this.remote_id,
    firstname = this.firstname,
    middlename = this.middlename,
    lastname = this.lastname,
    sex = MapUtil.stringMapping[this.sex],
    age = this.age,
    date_of_birth = this.dob,
    age_known = age_known?.lowercase() ?: "",
    dob_known = dob_known?.lowercase() ?: "",
    profile_picture = this.profile_picture,
    is_member_respondent = MapUtil.intMappings[this.is_member_respondent] ?: 0,
    is_head = MapUtil.intMappings[this.is_head] ?: 0,
    family_bond_id = MapUtil.intMappings[this.family_bond_id],
    marital_status_id = MapUtil.intMappings[this.marital_status_id] ?: 20,
    birth_certificate = MapUtil.intMappings[this.birth_certificate],
    educational_level_id = MapUtil.intMappings[this.educational_level_id] ?: 31,
    school_attendance_id = MapUtil.intMappings[this.school_attendance_id] ?: 46,
    pregnancy_status = MapUtil.intMappings[this.pregnancy_status],
    disability_id = MapUtil.intMappings[this.disability_id] ?: 55,
    socio_professional_category_id = MapUtil.intMappings[this.socio_professional_category_id]
        ?: 174,
    sector_of_work_id = MapUtil.intMappings[this.sector_of_work_id] ?: 175,
    household_id = this.household_id
)