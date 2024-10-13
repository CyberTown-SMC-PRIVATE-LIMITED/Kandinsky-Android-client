package bat.konst.kandinskyclient.ui.screens.newRequestScreen

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
class NewRequestScreenViewModel @Inject constructor(private val fbdataRepository: FbdataRepository): ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var state by mutableStateOf(NewRequestScreenState())
        private set

    fun onEvent(event: NewRequestScreenEvent, onSuccess: () -> Unit = {}) {
        when (event) {
            is NewRequestScreenEvent.StyleUpdate -> this.state = state.copy(style = event.newStyle)
            is NewRequestScreenEvent.PromptUpdate -> this.state = state.copy(prompt = event.newPrompt)
            is NewRequestScreenEvent.NegativePromptUpdate -> this.state = state.copy(negativePrompt = event.newNegativePrompt)
            is NewRequestScreenEvent.AddRequest -> {
                coroutineScope.launch(Dispatchers.Main) {
                    fbdataRepository.addRequest(state.prompt, state.negativePrompt, state.style, 1) {
                        onSuccess()
                    }
                }
            }
        }
    }
}
