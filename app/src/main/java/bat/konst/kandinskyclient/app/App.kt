package bat.konst.kandinskyclient.app

import android.app.Application
import bat.konst.kandinskyclient.data.room.FbdataRepository
import bat.konst.kandinskyclient.logic.worker.startImagesGeneratorWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject lateinit var fbdataRepository: FbdataRepository

    override fun onCreate() {
        FILE_STORAGE_PATH = filesDir.absolutePath
        super.onCreate()
        // start worker on app start
        startImagesGeneratorWorker(this)
        // set theme by db param
        CoroutineScope(Dispatchers.Main).launch {
            AppState.isDatkTheme = fbdataRepository.getConfigByName(CONFIG_THEME) == CONFIG_THEME_DARK
        }
    }

}