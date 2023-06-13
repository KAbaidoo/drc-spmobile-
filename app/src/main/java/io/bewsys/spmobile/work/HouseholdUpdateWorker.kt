package io.bewsys.spmobile.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.data.HouseholdEntity
import io.bewsys.spmobile.data.local.toPayload
import io.bewsys.spmobile.data.remote.model.household.HouseholdPayload
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.util.MapUtil
import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class HouseholdUpdateWorker(
    ctx: Context,
    params: WorkerParameters,
) : CoroutineWorker(ctx, params), KoinComponent {

    val repository: HouseholdRepository by inject()

    override suspend fun doWork(): Result {
        val id = inputData.getLong(KEY_DATA_ID, -1)
        return withContext(Dispatchers.IO) {

            try {
                if (id < 0) {
                    Log.e(TAG, "Invalid input id")
                    throw IllegalArgumentException("Invalid input id")
                }
                val item = getItem(id)
                uploadItem(item!!)


            } catch (throwable: Throwable) {
                Log.e(TAG, "Error uploading data")
                Result.failure()
            }!!

        }

    }


    private suspend fun uploadItem(item: HouseholdEntity): Result {
        return withContext(Dispatchers.IO) {
            var result: Result = Result.failure()

            item.apply {
                repository.updateHouseholdRemote(item.id,
                   item.toPayload()
                ).collectLatest { response ->
                    result = when (response) {
                        is Resource.Success -> {
                            Result.success()
                        }
                        is Resource.Exception -> {
                            response.throwable.localizedMessage?.let { Log.d(TAG, it) }
                            Result.failure()
                        }

                        else -> {
                            val res = response as Resource.Failure
                            Log.d(TAG, "${res.error}")
                            Result.failure()
                        }
                    }
                }
            }
            result
        }
    }




    private suspend fun getItem(id: Long): HouseholdEntity? =
        repository.getHousehold(id)
companion object{
    private const val TAG = "HouseholdUpdateWorker"
}

}