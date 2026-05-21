package com.muhdila.accurateuserapp.user.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.muhdila.accurateuserapp.user.domain.usecase.SyncUsersUseCase
import com.muhdila.accurateuserapp.core.domain.Result.Error
import com.muhdila.accurateuserapp.core.domain.Result.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncUsersUseCase: SyncUsersUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return when (syncUsersUseCase()) {
            is Success -> Result.success()
            is Error -> {
                if (runAttemptCount < MAX_RETRY_COUNT) Result.retry()
                else Result.failure()
            }
        }
    }

    companion object {
        const val WORK_NAME = "SyncWorker"
        private const val MAX_RETRY_COUNT = 3
    }
}
