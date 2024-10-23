package bat.konst.kandinskyclient.worker

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

// https://vtsen.hashnode.dev/simple-example-to-use-workmanager-and-notification
// https://dev.to/vtsen/simple-example-to-use-workmanager-and-notification-il9
// https://readmedium.com/android-workmanager-in-clean-architecture-393ce4f27ef5
// hilt example - https://www.droidcon.com/2022/06/14/when-jetpacks-glance-met-his-fellow-worker-work-manager/
@HiltWorker
class ImagesGeneratorWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters
    // private val fbdataRepository: FbdataRepository,
    // private val kandinskyApiRepository: KandinskyApiRepository
): CoroutineWorker(context, workerParameters) { // m.b. CoroutineWorker
    /*
        Использование:
        startImagesGeneratorWorker(context)
     */
    //@Inject lateinit var kandinskyApiRepository: KandinskyApiRepository
    //@Inject lateinit var fbdataRepository: FbdataRepository

    override suspend fun doWork(): Result {
        doKandinskyRequests()
        return Result.success()
    }

    private suspend fun doKandinskyRequests() {
        // забираем репозитории. Почему-то через Inject падает
        // TODO: разаобраться почему и переделать
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
