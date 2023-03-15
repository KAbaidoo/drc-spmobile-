package io.bewsys.spmobile.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.data.NonConsentHouseholdEntity
import io.bewsys.spmobile.data.remote.model.noconsent.NonConsentHouseholdPayload
import io.bewsys.spmobile.data.repository.NonConsentingHouseholdRepository
import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


private const val TAG = "NonConsentUploadWorker"

class NonConsentUploadWorker(
    ctx: Context,
    params: WorkerParameters,
) : CoroutineWorker(ctx, params), KoinComponent {

    val repository: NonConsentingHouseholdRepository by inject()

    override suspend fun doWork(): Result {
        val id = inputData.getLong(KEY_DATA_ID, -1)
        return withContext(Dispatchers.IO) {

            try {
                if (id < 0) {
                    Log.e(TAG, "Invalid input id")
                    throw IllegalArgumentException("Invalid input id")
                }
                val item = getItem(id)
                item?.let { uploadItem(item) }


            } catch (throwable: Throwable) {
                Log.e(TAG, "Error uploading data")
                Result.failure()
            }!!

        }

    }


    private suspend fun uploadItem(item: NonConsentHouseholdEntity): Result {
        return withContext(Dispatchers.IO) {
            var result: Result = Result.failure()
            repository.uploadNonConsentingHousehold(
                NonConsentHouseholdPayload(
                    item.id,
                    item.province_id,
                    item.community_id,
                    item.territory_id,
                    item.groupement_id,
                    item.address,
                    item.gps_longitude,
                    item.gps_latitude,
                    item.reason,
                    item.other_non_consent_reason,
                    item.status
                )
            ).collectLatest { response ->
                result = when (response) {
                    is Resource.Success -> {
                        updateItem(item.id)
                        Result.success()
                    }
                    is Resource.Exception -> {
                        Result.failure()
                    }
                    else -> {
                        Result.failure()
                    }

                }
            }
            result
        }
    }


    private suspend fun updateItem(id: Long) {
        delay(2000L)
        repository.updateStatus("submitted", id)
    }

    private suspend fun getItem(id: Long): NonConsentHouseholdEntity? =
        repository.getNonConsentingHousehold(id)


}

