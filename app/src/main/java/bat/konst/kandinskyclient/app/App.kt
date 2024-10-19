package bat.konst.kandinskyclient.app

import android.app.Application
import bat.konst.kandinskyclient.worker.startImagesGeneratorWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        FILE_STORAGE_PATH = filesDir.absolutePath
        super.onCreate()
        // start worker on app start
        startImagesGeneratorWorker(this)
    }

}