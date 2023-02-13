package io.bewsys.spmobile.data.repository

import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.QueryResult.Unit.value
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.NonConsentHouseholdEntity
import io.bewsys.spmobile.data.model.NonConsentHousehold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class NonConsentingHouseholdRepositoryImpl(db: Database) : INonConsentingHouseholdRepository {

    private val queries = db.nonConsentHouseholdQueries

    override suspend fun getAllNonConsentingHouseholds(): Flow<List<NonConsentHouseholdEntity>> =
        queries.getAllNonConsentHouseholds().asFlow().mapToList(context = Dispatchers.Default)


    override suspend fun getNonConsentingHousehold(id: Long): NonConsentHouseholdEntity? =
        withContext(Dispatchers.IO) {
            queries.getById(id).executeAsOneOrNull()
        }

    override suspend fun insertNonConsentingHousehold(
        newNonConsentingHouseholds: NonConsentHousehold
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

    override suspend fun getLastInsertedRowId(): Long = withContext(Dispatchers.IO) {
         queries.lastInsertRowId().executeAsOne()

    }

    override suspend fun updateStatus( status: String,id: Long) {
        queries.updateNonConsentHousehold(status,id)
    }

}