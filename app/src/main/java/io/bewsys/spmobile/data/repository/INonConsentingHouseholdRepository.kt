package io.bewsys.spmobile.data.repository

import io.bewsys.spmobile.data.NonConsentHouseholdEntity
import io.bewsys.spmobile.data.model.NonConsentHousehold
import kotlinx.coroutines.flow.Flow


interface INonConsentingHouseholdRepository {
    suspend fun getAllNonConsentingHouseholds(): Flow<List<NonConsentHouseholdEntity>>

    suspend fun getNonConsentingHousehold(id: Long): NonConsentHouseholdEntity?

    suspend fun insertNonConsentingHousehold(
        newNonConsentingHouseholds: NonConsentHousehold
    ): Unit

    suspend fun getLastInsertedRowId(): Long

    suspend fun updateStatus(status: String, id: Long)
}