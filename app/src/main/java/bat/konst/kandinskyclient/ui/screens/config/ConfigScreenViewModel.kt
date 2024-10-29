package bat.konst.kandinskyclient.ui.screens.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bat.konst.kandinskyclient.app.CONFIG_DEFAULT_VALUE
import bat.konst.kandinskyclient.app.CONFIG_XKEY
import bat.konst.kandinskyclient.app.CONFIG_XSECRET
import bat.konst.kandinskyclient.app.KANDINSKY_MODEL_ID_UNDEFINED
import bat.konst.kandinskyclient.data.kandinskyApi.KandinskyApiRepository
import bat.konst.kandinskyclient.data.room.FbdataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigScreenViewModel @Inject constructor(
    private val fbdataRepository: FbdataRepository,
    private val kandinskyApiRepository: KandinskyApiRepository,
): ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var state by mutableStateOf(ConfigScreenState())
        private set

    fun onEvent(event: ConfigScreenEvent, onSuccess: () -> Unit = {}, onError: () -> Unit = {})  {
        when (event) {

            is ConfigScreenEvent.KeyUpdate -> state = state.copy(key = event.newKey)

            is ConfigScreenEvent.SecretUpdate -> state = state.copy(secret = event.newSecret)

            is ConfigScreenEvent.SaveConfig -> {
                coroutineScope.launch {
                    // 1. Проверяем корректность ключей - для этого пробуем запросить текущую версию модели FusionBrain
                    val fusionBrainModelVersionId: String = kandinskyApiRepository.getModelVersionId("Key " + state.key, "Secret " + state.secret)
                    if (fusionBrainModelVersionId == KANDINSKY_MODEL_ID_UNDEFINED) {
                        onError()
                        return@launch
                    }
                    // 2. Сохраняем конфиг
                    fbdataRepository.setConfig(CONFIG_XKEY, state.key) {
                        coroutineScope.launch {
                            fbdataRepository.setConfig(CONFIG_XSECRET, state.secret) {
                                onSuccess()
                            }
                        }
                    }
                }
            }

            is ConfigScreenEvent.LoadConfig -> {
                coroutineScope.launch {
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
