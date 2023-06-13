package io.bewsys.spmobile.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.NonConsentHouseholdEntity
import io.bewsys.spmobile.data.local.NonConsentHouseholdModel
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.remote.NonConsentingHouseholdApi
import io.bewsys.spmobile.data.remote.model.noconsent.NonConsentHouseholdPayload
import io.bewsys.spmobile.data.remote.model.noconsent.FailureMessage

import io.bewsys.spmobile.util.Resource
import io.ktor.client.call.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

private const val TAG = "NON_CONSENT_REPOSITORY"

class NonConsentingHouseholdRepository(
    db: Database,
    private val api: NonConsentingHouseholdApi,
    private val preferencesManager: PreferencesManager
) {

    private val queries = db.nonConsentHouseholdQueries

    suspend fun getAllNonConsentingHouseholds(): Flow<List<NonConsentHouseholdEntity>> =
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
    suspend fun updateNonConsentingHousehold(id: Long,
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


//    suspend fun getLastInsertedRowId(): Long = withContext(Dispatchers.IO) {
//        queries.lastInsertRowId().executeAsOne()
//    }

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
}