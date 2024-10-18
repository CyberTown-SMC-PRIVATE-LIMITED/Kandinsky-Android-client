package bat.konst.kandinskyclient.model

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

// https://www.droidcon.com/2022/06/14/when-jetpacks-glance-met-his-fellow-worker-work-manager/
class ImagesGeneratorWorker(context: Context, workerParameters: WorkerParameters):
    /*
        Использование:
        PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.MINUTES)
            .setConstraints(powerConstraints)
            .setInputData(taskData)
            .build()
        worker.enqueueUniquePeriodicWork("kandinsky_worker", ExistingPeriodicWorkPolicy.KEEP, work)
     */
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        doSomething()
        val outputData = Data.Builder().putString(WORK_RESULT, "Task Finished").build()
        return Result.success(outputData)
    }

    companion object {
        const val WORK_RESULT = "work_result"
    }

    private fun doSomething() {
        Log.d("KandinskyWorker", "Do something")
    }
}