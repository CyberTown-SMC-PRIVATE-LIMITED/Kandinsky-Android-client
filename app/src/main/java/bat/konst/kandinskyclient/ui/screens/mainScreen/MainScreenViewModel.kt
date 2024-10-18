package bat.konst.kandinskyclient.ui.screens.mainScreen

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bat.konst.kandinskyclient.data.room.FbdataRepository
import bat.konst.kandinskyclient.model.startImagesGeneratorWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val fbdataRepository: FbdataRepository
): ViewModel() {

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

        }
    }
}
