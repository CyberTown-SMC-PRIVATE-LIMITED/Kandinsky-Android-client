package bat.konst.kandinskyclient.ui.screens.request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bat.konst.kandinskyclient.data.room.FbdataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestScreenViewModel @Inject constructor(private val fbdataRepository: FbdataRepository): ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var state by mutableStateOf(RequestScreenState())
        private set

    fun onEvent(event: RequestScreenEvent)  {
        when (event) {

            is RequestScreenEvent.ScreenUpdate -> {
                // обновление данных
                coroutineScope.launch(Dispatchers.Main) {
                    val request = fbdataRepository.getRequest(event.md5)
                    val images = fbdataRepository.getImages(event.md5)
                    state = state.copy(
                        md5 = request.md5,
                        prompt = request.prompt,
                        negativePrompt = request.negativePrompt,
                        style = request.style,
                        images = images,
                        hasQueuedImages = fbdataRepository.hasQueuedImages()
                    )
                }
            }

        }
    }
}