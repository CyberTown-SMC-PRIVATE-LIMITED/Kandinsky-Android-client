package bat.konst.kandinskyclient.ui.screens.main

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bat.konst.kandinskyclient.app.AppState
import bat.konst.kandinskyclient.app.CONFIG_XKEY
import bat.konst.kandinskyclient.app.CONFIG_XSECRET
import bat.konst.kandinskyclient.data.room.FbdataRepository
import bat.konst.kandinskyclient.logic.worker.startImagesGeneratorWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val fbdataRepository: FbdataRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var state by mutableStateOf(MainScreenState())
        private set

    fun onEvent(event: MainScreenEvent)  {
        when (event) {

            is MainScreenEvent.ChangeTheme -> {
                AppState.isDatkTheme = !AppState.isDatkTheme
            }

            is MainScreenEvent.ScreenUpdate -> {
                coroutineScope.launch(Dispatchers.Main) {
                    state = state.copy(
                        key = fbdataRepository.getConfigByName(CONFIG_XKEY),
                        secret = fbdataRepository.getConfigByName(CONFIG_XSECRET),
                        requests = fbdataRepository.getAllRequestJoinImages(),
                        hasQueuedImages = fbdataRepository.hasQueuedImages()
                    )
                    // если что-то есть на генерацию -- отправим сигнал воркеру
                    if (fbdataRepository.hasQueuedImages()) {
                        startImagesGeneratorWorker(context)
                    }
                }
            }

            is MainScreenEvent.RequestDelete -> {
                coroutineScope.launch(Dispatchers.Main) {
                    fbdataRepository.markDeletedRequest(event.md5)
                    state = state.copy(
                        requests = fbdataRepository.getAllRequestJoinImages(),
                        hasQueuedImages = fbdataRepository.hasQueuedImages()
                    )
                    startImagesGeneratorWorker(context)  // запустим Worker чтобы удалить помеченные данные из БД
                }
            }

        }
    }
}
