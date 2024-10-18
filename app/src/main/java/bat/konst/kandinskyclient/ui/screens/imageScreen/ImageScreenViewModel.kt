package bat.konst.kandinskyclient.ui.screens.imageScreen

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
class ImageScreenViewModel @Inject constructor(private val fbdataRepository: FbdataRepository): ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var state by mutableStateOf(ImageScreenState())
        private set

    fun onEvent(event: ImageScreenEvent)  {
        when (event) {

            is ImageScreenEvent.ScreenUpdate -> {
                coroutineScope.launch(Dispatchers.Main) {
                    val image = fbdataRepository.getImage(event.id)
                    state = state.copy(
                        id = image.id,
                        status = image.status,
                        dateCreated = image.dateCreated,
                        imageBase64 = image.imageBase64,
                        prevImageId = fbdataRepository.getPrevImageId(image.md5, image.id),
                        nextImageId = fbdataRepository.getNextImageId(image.md5, image.id)
                    )
                }
            }

        }
    }
}