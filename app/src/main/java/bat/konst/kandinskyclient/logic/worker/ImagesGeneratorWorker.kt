package bat.konst.kandinskyclient.logic.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import bat.konst.kandinskyclient.di.FbdataModule
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class ImagesGeneratorWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        doKandinskyRequests()
        return Result.success()
    }

    private suspend fun doKandinskyRequests() {
        // получаем репозитории
        val bu = bat.konst.kandinskyclient.di.KandinskyApiModule.baseUrl()
        val kd = bat.konst.kandinskyclient.di.KandinskyApiModule.provideRetrofit(bu)
        val kandinskyApiRepository = bat.konst.kandinskyclient.di.KandinskyApiModule.provideKandinskyApiRepository(kd)

        val fdb = FbdataModule.provideFbdataDatabase(context)
        val fd = FbdataModule.provideFbdataDao(fdb)
        val fbdataRepository = FbdataModule.provideFbdataRepository(fd)

        ImagesGenerator().fusionBrainGo(fbdataRepository, kandinskyApiRepository, context)
    }
}

fun startImagesGeneratorWorker(context: Context) {
    val immageWorkRequest = OneTimeWorkRequest.Builder(ImagesGeneratorWorker::class.java)
        .build()
    // Schedule the WorkRequest with WorkManager
    WorkManager.getInstance(context).enqueueUniqueWork("KandinskyWorker", ExistingWorkPolicy.KEEP, immageWorkRequest)
}
