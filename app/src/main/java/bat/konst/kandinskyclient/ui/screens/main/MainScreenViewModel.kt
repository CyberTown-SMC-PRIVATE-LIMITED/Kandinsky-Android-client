package bat.konst.kandinskyclient.ui.screens.main

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
                    state = state.copy(requests = fbdataRepository.getAllRequestJoinImages())
                }
            }

            is MainScreenEvent.RequestDelete -> {
                coroutineScope.launch(Dispatchers.Main) {
                    fbdataRepository.markDeletedRequest(event.md5)
                    state = state.copy(requests = fbdataRepository.getAllRequestJoinImages())
                }
            }

        }
    }
}
