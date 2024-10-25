package bat.konst.kandinskyclient.ui.screens.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bat.konst.kandinskyclient.app.CONFIG_DEFAULT_VALUE
import bat.konst.kandinskyclient.app.CONFIG_XKEY
import bat.konst.kandinskyclient.app.CONFIG_XSECRET
import bat.konst.kandinskyclient.data.room.FbdataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigScreenViewModel @Inject constructor(private val fbdataRepository: FbdataRepository): ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var state by mutableStateOf(ConfigScreenState())
        private set

    fun onEvent(event: ConfigScreenEvent, onSuccess: () -> Unit = {})  {
        when (event) {

            is ConfigScreenEvent.KeyUpdate -> state = state.copy(key = event.newKey)

            is ConfigScreenEvent.SecretUpdate -> state = state.copy(secret = event.newSecret)

            is ConfigScreenEvent.SaveConfig -> {
                coroutineScope.launch(Dispatchers.Main) {
                    fbdataRepository.setConfig(CONFIG_XKEY, state.key) {
                        coroutineScope.launch(Dispatchers.Main) {
                            fbdataRepository.setConfig(CONFIG_XSECRET, state.secret) {
                                onSuccess()
                            }
                        }
                    }
                }
            }

            is ConfigScreenEvent.LoadConfig -> {
                coroutineScope.launch(Dispatchers.Main) {
                    state = state.copy(
                        key = fbdataRepository.getConfigByName(CONFIG_XKEY),
                        secret = fbdataRepository.getConfigByName(CONFIG_XSECRET)
                    )
                    onSuccess()
                }
            }

        }
    }
}
