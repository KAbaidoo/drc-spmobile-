package io.bewsys.spmobile.data.repository

import io.bewsys.spmobile.data.Household
import io.bewsys.spmobile.data.NonConsentHouseholdEntity
import io.bewsys.spmobile.data.model.HouseholdModel
import io.bewsys.spmobile.data.model.NonConsentHousehold
import kotlinx.coroutines.flow.Flow


interface HouseholdRepository {
    suspend fun getAllHouseholds(): Flow<List<Household>>

    suspend fun getHousehold(id: Long): Household?

    suspend fun insertHousehold(
        household: HouseholdModel
    ): Unit

    suspend fun getLastInsertedRowId(): Long

    suspend fun updateStatus(status: String, id: Long)
}