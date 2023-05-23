package io.bewsys.spmobile.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.HouseholdEntity
import io.bewsys.spmobile.data.MemberEntity
import io.bewsys.spmobile.data.local.MemberModel
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.remote.MemberApi
import io.bewsys.spmobile.data.remote.model.auth.login.ErrorResponse
import io.bewsys.spmobile.data.remote.model.dashboard.Groupment
import io.bewsys.spmobile.data.remote.model.member.MemberPayload
import io.bewsys.spmobile.util.ApplicationScope
import io.bewsys.spmobile.util.Resource
import io.ktor.client.call.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class MemberRepository(
    db: Database,
    private val api: MemberApi,
    private val preferencesManager: PreferencesManager,
    @ApplicationScope private val applicationScope: CoroutineScope
) {
    private val householdQueries = db.householdQueries
    private val membersQueries = db.householdMemberQueries


    val getMembersCount =
        membersQueries.getHouseholdMembersCount().asFlow().mapToOne(Dispatchers.Default)


    suspend fun getHousehold(id: Long): HouseholdEntity? =
        withContext(Dispatchers.IO) {
            householdQueries.getById(id).executeAsOneOrNull()
        }

    suspend fun insertMembers(
        membersList: List<MemberModel>
    ): Unit = withContext(Dispatchers.IO) {
        membersList.forEach { memberModel ->
            memberModel.also {
                membersQueries.insertMember(
                    null,
                    remote_id = it.remote_id,
                    firstname = it.firstname,
                    lastname = it.lastname,
                    middlename = it.middlename,
                    age = it.age,
                    profile_picture = it.profile_picture,
                    sex = it.sex,
                    dob = it.dob,
                    date_of_birth = it.dob,
                    age_known = it.age_known,
                    dob_known = it.dob_known,
                    is_head = it.is_head,
                    is_member_respondent = it.is_member_respondent,
                    family_bond_id = it.family_bond_id,
                    marital_status_id = it.marital_status_id,
                    birth_certificate = it.birth_certificate,
                    educational_level_id = it.educational_level_id,
                    school_attendance_id = it.school_attendance_id,
                    pregnancy_status = it.pregnancy_status,
                    disability_id = it.disability_id,
                    socio_professional_category_id = it.socio_professional_category_id,
                    sector_of_work_id = it.sector_of_work_id,
                    household_id = it.household_id,
                    status = it.status
                )
            }

        }
    }


    suspend fun getMemberByHousehold(householdId: String): Flow<List<MemberEntity>> =
        membersQueries.getByHouseholdId(householdId).asFlow()
            .mapToList(context = Dispatchers.Default)



    suspend fun deleteMember(id: Long) = withContext(Dispatchers.IO) {
        membersQueries.deleteMember(id)
    }

    suspend fun getLastInsertedRowId(): Long = withContext(Dispatchers.IO) {
        membersQueries.lastInsertRowId().executeAsOne()
    }

//    suspend fun updateStatus(status: String, id: Long, householdId: String, remote_id: String) {
//        membersQueries.updateStatus(id = id, status = status, remoteId = remote_id)
//    }

    suspend fun getMemberByHouseholdId(householdId: String): Flow<List<MemberEntity>> =
        membersQueries.getByHouseholdId(householdId).asFlow()
            .mapToList(context = Dispatchers.Default)

    //    ================================================================
    //   *********************** network calls  ************************
    suspend fun uploadMember(payload: MemberPayload, householdId: String) = flow {

        try {
            val userPref = preferencesManager.preferencesFlow.first()
            val response = api.uploadMember(payload, userPref.token, householdId)
            if (response.status.value in 200..299) {
                emit(Resource.Success<Any>(response.body()))
            } else {
                emit(Resource.Failure<ErrorResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)




}