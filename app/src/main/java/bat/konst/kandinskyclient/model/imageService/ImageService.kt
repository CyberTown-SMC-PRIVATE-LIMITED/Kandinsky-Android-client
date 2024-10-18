package bat.konst.kandinskyclient.model.imageService

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ImageService : Service() {
    // https://www.geeksforgeeks.org/services-in-android-using-jetpack-compose/
    // в Манифест добавить <service android:name=".model.imageService.ImageService" />
    private var job: Job? = null  // корутина для работы с KandiskyApi

    private fun doSomething() {
        Log.d("ImageService", "doSomething")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ImageService", "Service starts")
        job = CoroutineScope(Dispatchers.IO).launch() {
            while (isActive) {
                doSomething()
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