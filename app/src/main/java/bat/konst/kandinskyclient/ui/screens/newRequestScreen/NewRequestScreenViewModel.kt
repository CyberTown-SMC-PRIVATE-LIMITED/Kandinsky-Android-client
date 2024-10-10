package bat.konst.kandinskyclient.ui.screens.newRequestScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class NewRequestScreenViewModel : ViewModel() {
    var state by mutableStateOf(NewRequestScreenState())
        private set

    fun onEvent(event: NewRequestScreenEvent)  {
        when (event) {
            is NewRequestScreenEvent.StyleUpdate -> this.state = state.copy(style = event.newStyle)
            is NewRequestScreenEvent.PromptUpdate -> this.state = state.copy(prompt = event.newPrompt)
            is NewRequestScreenEvent.NegativePromptUpdate -> this.state = state.copy(negativePrompt = event.newNegativePrompt)
        }
    }
}
