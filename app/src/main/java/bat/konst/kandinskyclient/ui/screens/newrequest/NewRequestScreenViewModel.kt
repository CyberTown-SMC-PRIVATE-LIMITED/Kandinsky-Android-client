package bat.konst.kandinskyclient.ui.screens.newrequest

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bat.konst.kandinskyclient.data.kandinskyApi.KandinskyApiRepository
import bat.konst.kandinskyclient.data.kandinskyApi.models.Styles
import bat.konst.kandinskyclient.data.room.FbdataRepository
import bat.konst.kandinskyclient.worker.startImagesGeneratorWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewRequestScreenViewModel @Inject constructor(
    private val fbdataRepository: FbdataRepository,
    private val kandinskyApiRepository: KandinskyApiRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    var state by mutableStateOf(NewRequestScreenState())
        private set

    fun onEvent(event: NewRequestScreenEvent, onSuccess: () -> Unit = {}) {
        when (event) {

            is NewRequestScreenEvent.StyleUpdate -> {
                this.state = state.copy(
                    style = event.newStyle,
                    styleImageURL = getStyleURL(event.newStyle, state.stylesList)
                )
            }

            is NewRequestScreenEvent.PromptUpdate -> this.state = state.copy(prompt = event.newPrompt)

            is NewRequestScreenEvent.NegativePromptUpdate -> this.state = state.copy(negativePrompt = event.newNegativePrompt)

            is NewRequestScreenEvent.AddRequest -> {
                coroutineScope.launch(Dispatchers.Main) {
                    fbdataRepository.addRequest(event.prompt, event.negativePrompt, event.style, event.qw) {
                        // Запускаем Worker - поскольку у нас появились задания на генерацию
                        startImagesGeneratorWorker(context)
                        onSuccess()
                    }
                }
            }

            is NewRequestScreenEvent.ScreenUpdate -> {
                coroutineScope.launch(Dispatchers.Main) {
                    // Получение данных по запросу если ключ (md5) задан
                    val request = fbdataRepository.getRequest(event.md5)
                    // обновление StylesList
                    val styleList = kandinskyApiRepository.getStyles()
                    var style = request.style
                    if (style == "") { // это новый запрос -- поэтому стиль первый из списка всех доступных
                        style = if (styleList.isEmpty()) "" else styleList[0].name
                    }
                    state = state.copy(
                        prompt = request.prompt,
                        negativePrompt = request.negativePrompt,
                        stylesList = styleList,
                        style = style,
                        styleImageURL = getStyleURL(style, styleList)
                    )
                }
            }

        }
    }

    // --------------------
    private fun getStyleURL(styleName: String, styleList: Styles): String {
        // получает URL картинки по имени стиля
        var imageURL = ""
        for (style in styleList) {
            if (style.name == styleName) {
                imageURL = style.image
            }
        }
        return imageURL
    }

}
