package bat.konst.kandinskyclient.app

import android.app.Application
import android.content.Intent
import bat.konst.kandinskyclient.model.imageService.ImagesGeneratorService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        FILE_STORAGE_PATH = filesDir.absolutePath
        super.onCreate()

        // start Kandinsky Update Service
        startService(Intent(this, ImagesGeneratorService::class.java))
    }

}