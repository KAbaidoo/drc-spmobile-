package io.bewsys.spmobile.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.data.NonConsentHouseholdEntity
import io.bewsys.spmobile.data.repository.NonConsentingHouseholdRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


private const val TAG = "UploadWorker"

class UploadWorker(
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

//                uploadItem(item)

                updateItem(id)






                Result.success()
            } catch (throwable: Throwable) {
                Log.e(TAG, "Error uploading data")
                Result.failure()
            }

        }


    }

    private suspend fun uploadItem(item: NonConsentHouseholdEntity?) {

    }

    private suspend fun updateItem(id: Long) {
        delay(2000L)
        repository.updateStatus("submitted", id)

    }

    private suspend fun getItem(id: Long): NonConsentHouseholdEntity? =
        repository.getNonConsentingHousehold(id)


}

