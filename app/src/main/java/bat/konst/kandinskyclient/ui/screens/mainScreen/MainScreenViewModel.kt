package bat.konst.kandinskyclient.ui.screens.mainScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainScreenViewModel : ViewModel() {
    var state by mutableStateOf(MainScreenState())
        private set

    fun onEvent(event: MainScreenEvent)  {
        when (event) {
            // пример обработки events
            is MainScreenEvent.EmailUpdate -> this.state = state.copy(email = event.newEmail)
            is MainScreenEvent.PasswordUpdate -> this.state = state.copy(password = event.newPassword)
        }
    }
}
