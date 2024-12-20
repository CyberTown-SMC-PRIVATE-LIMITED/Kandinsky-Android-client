package bat.konst.kandinskyclient.ui.screens.request

import bat.konst.kandinskyclient.data.room.entity.Image

sealed class RequestScreenEvent {
    data class ScreenUpdate(val md5: String) : RequestScreenEvent() // получение данных по запросу
}

data class RequestScreenState(
    val md5: String = "",
    val prompt: String = "",
    val negativePrompt: String = "",
    val style: String = "",
    val images: List<Image> = emptyList(),
    val hasQueuedImages: Boolean = false
)
