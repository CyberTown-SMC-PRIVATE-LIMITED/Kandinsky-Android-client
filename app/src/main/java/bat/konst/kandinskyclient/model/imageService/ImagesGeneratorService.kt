package bat.konst.kandinskyclient.model.imageService

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import bat.konst.kandinskyclient.data.kandinskyApi.KandinskyApiRepository
import bat.konst.kandinskyclient.data.room.FbdataRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImagesGeneratorService: Service() {
    // https://www.geeksforgeeks.org/services-in-android-using-jetpack-compose/
    // в Манифест добавить <service android:name=".model.imageService.ImageService" />
    // сервис - раз в 10 секунд обрабатывает запросы к KandiskyApi
    @Inject lateinit var kandinskyApiRepository: KandinskyApiRepository
    @Inject lateinit var fbdataRepository: FbdataRepository
    private var job: Job? = null  // корутина для работы с KandiskyApi

    private suspend fun generateImages() {
        Log.d("ImageService", "generateImages")
        ImagesGenerator().FusionBrainGo(fbdataRepository, kandinskyApiRepository)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ImageService", "Service starts")
        job = CoroutineScope(Dispatchers.IO).launch() {
            while (isActive) {
                generateImages()
                delay(10000) // Задержка на 10 секунд
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d("ImageService", "Destroyed")
        super.onDestroy()
        job?.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}