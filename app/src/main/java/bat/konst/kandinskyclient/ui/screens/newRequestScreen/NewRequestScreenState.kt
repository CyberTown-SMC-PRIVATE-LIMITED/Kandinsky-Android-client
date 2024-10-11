package bat.konst.kandinskyclient.ui.screens.newRequestScreen

sealed class NewRequestScreenEvent {
    data class StyleUpdate(val newStyle: String): NewRequestScreenEvent()
    data class PromptUpdate(val newPrompt: String): NewRequestScreenEvent()
    data class NegativePromptUpdate(val newNegativePrompt: String): NewRequestScreenEvent()
    data class AddRequest(val prompt: String, val negativePrompt: String, val style: String): NewRequestScreenEvent()
}

data class NewRequestScreenState(
    val style: String = "DEFAULT",
    val prompt: String = "",
    val negativePrompt: String = "",
)
