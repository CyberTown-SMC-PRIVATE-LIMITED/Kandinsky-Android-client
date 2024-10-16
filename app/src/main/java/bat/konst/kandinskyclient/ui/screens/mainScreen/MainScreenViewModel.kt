package bat.konst.kandinskyclient.ui.screens.mainScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bat.konst.kandinskyclient.data.kandinskyApi.KandinskyApiRepository
import bat.konst.kandinskyclient.data.room.FbdataRepository
import bat.konst.kandinskyclient.model.imageWorker.GenerateImages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val fbdataRepository: FbdataRepository, private val kandinskyApiRepository: KandinskyApiRepository): ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var state by mutableStateOf(MainScreenState())
        private set

    fun onEvent(event: MainScreenEvent)  {
        when (event) {

            is MainScreenEvent.ScreenUpdate -> {
                coroutineScope.launch(Dispatchers.Main) {
                    state = state.copy(requests = fbdataRepository.getAllRequests())
                }
            }

            is MainScreenEvent.TestWorker -> {
                // временная заглушка для тестирования Worker
                coroutineScope.launch(Dispatchers.IO) {
                    GenerateImages().FusionBrainGo(fbdataRepository, kandinskyApiRepository)
                }

            }
        }
    }
}
