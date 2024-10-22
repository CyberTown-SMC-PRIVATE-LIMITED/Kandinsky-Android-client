package bat.konst.kandinskyclient.ui.screens.newRequestScreen

import bat.konst.kandinskyclient.data.kandinskyApi.models.Styles
import bat.konst.kandinskyclient.ui.screens.mainScreen.MainScreenEvent

sealed class NewRequestScreenEvent {
    data class StyleUpdate(val newStyle: String): NewRequestScreenEvent()
    data class PromptUpdate(val newPrompt: String): NewRequestScreenEvent()
    data class NegativePromptUpdate(val newNegativePrompt: String): NewRequestScreenEvent()
    data class AddRequest(val prompt: String, val negativePrompt: String, val style: String, val qw: Int): NewRequestScreenEvent()
    data class ScreenUpdate(val md5: String): NewRequestScreenEvent()
}

data class NewRequestScreenState(
    val style: String = "DEFAULT",
    val prompt: String = "",
    val negativePrompt: String = "",
    val stylesList: Styles = Styles(), // список стилей
    val styleImageURL: String = "", // URL картинки стиля
)
