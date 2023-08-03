package io.bewsys.spmobile.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.NonConsentHouseholdEntity
import io.bewsys.spmobile.data.local.NonConsentHouseholdModel
import io.bewsys.spmobile.data.local.toPayLoad
import io.bewsys.spmobile.data.local.toPayload
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.remote.NonConsentingHouseholdApi
import io.bewsys.spmobile.data.remote.model.auth.login.ErrorResponse
import io.bewsys.spmobile.data.remote.model.household.BulkUploadResponse
import io.bewsys.spmobile.data.remote.model.noconsent.NonConsentHouseholdPayload
import io.bewsys.spmobile.data.remote.model.noconsent.FailureMessage

import io.bewsys.spmobile.util.Resource
import io.ktor.client.call.*
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val TAG = "NON_CONSENT_REPOSITORY"

class NonConsentingHouseholdRepository(
    db: Database,
    private val api: NonConsentingHouseholdApi,
    private val preferencesManager: PreferencesManager
) {

    private val queries = db.nonConsentHouseholdQueries

     fun getAllNonConsentingHouseholds(): Flow<List<NonConsentHouseholdEntity>> =
        queries.getAllNonConsentHouseholds().asFlow().mapToList(context = Dispatchers.Default)


    suspend fun getNonConsentingHousehold(id: Long): NonConsentHouseholdEntity? =
        withContext(Dispatchers.IO) {
            queries.getById(id).executeAsOneOrNull()
        }

    suspend fun insertNonConsentingHousehold(
        newNonConsentingHouseholds: NonConsentHouseholdModel
    ): Unit = withContext(Dispatchers.IO) {

        newNonConsentingHouseholds.apply {
            queries.insertNonConsentHousehold(
                id,
                province_id,
                community_id,
                territory_id,
                groupement_id,
                gps_longitude,
                gps_latitude,
                reason,
                address,
                other_non_consent_reason,
                status
            )
        }
    }

    suspend fun updateNonConsentingHousehold(
        id: Long,
        newNonConsentingHouseholds: NonConsentHouseholdModel
    ): Unit = withContext(Dispatchers.IO) {

        newNonConsentingHouseholds.apply {

            queries.insertNonConsentHousehold(
                id,
                province_id,
                community_id,
                territory_id,
                groupement_id,
                gps_longitude,
                gps_latitude,
                reason,
                address,
                other_non_consent_reason,
                status
            )
        }
    }


    suspend fun getLastInsertedRowId(): Long = withContext(Dispatchers.IO) {
        queries.lastInsertRowId().executeAsOne()
    }

    suspend fun updateStatus(status: String, id: Long) {
        queries.updateNonConsentHousehold(status, id)
    }

    //    ================================================================
    //   *********************** network calls  ************************
    suspend fun uploadNonConsentingHousehold(payload: NonConsentHouseholdPayload) = flow {

        try {
            val userPref = preferencesManager.preferencesFlow.first()
            val response = api.uploadNonConsentHousehold(payload, userPref.token)

            if (response.status.value in 200..299) {
                emit(Resource.Success<Any>(response.body()))
            } else {
                emit(Resource.Failure<FailureMessage>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)




        suspend fun bulkUploadNonConsentHouseholdsFlow() = flow {
            emit(Resource.Loading)

            getAllNonConsentingHouseholds().map {list->
                list.filter { e -> e.status != "submitted" }.map { it.toPayload() }
            }.map {
                nonConsentList->

                if(nonConsentList.isEmpty()){
                    emit(Resource.Failure<ErrorResponse>(ErrorResponse(msg = "Nothing to upload!")))
                }

                nonConsentList.forEach { 
                    try {
                        val userPref = preferencesManager.preferencesFlow.first()

                        val response =
                            api.uploadNonConsentHousehold(it, userPref.token)
                        if (response.status.value in 200..299) {

                            emit(Resource.Success<Any>(response.body()))
                            withContext(Dispatchers.IO) {
                                updateStatus("submitted", it.id!!)
                            }
                        } else {
                            emit(Resource.Failure<ErrorResponse>(response.body()))
                        }

                    } catch (throwable: Throwable) {
                        emit(Resource.Exception(throwable, null))
                    }
                }


            }.first()
        }.flowOn(Dispatchers.IO)

}