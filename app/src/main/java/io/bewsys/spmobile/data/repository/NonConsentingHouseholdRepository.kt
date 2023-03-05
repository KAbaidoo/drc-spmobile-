package io.bewsys.spmobile.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.NonConsentHouseholdEntity
import io.bewsys.spmobile.data.local.NonConsentHouseholdModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class NonConsentingHouseholdRepository(db: Database) {

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
                gps_longitude,
                gps_latitude,
                reason,
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

}