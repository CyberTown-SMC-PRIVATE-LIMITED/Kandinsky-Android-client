package bat.konst.kandinskyclient.ui.screens.imageScreen

sealed class ImageScreenEvent {
    data class ScreenUpdate(val id: Long): ImageScreenEvent()
}

data class ImageScreenState(
    val openKey: Long = 0, // ключ для запуска события открытия экрана
    val md5: String = "",
    val id: Long = 0,
    val status: Int = 0,
    val dateCreated: Long = 0,
    val imageBase64: String = "",
    val prevImageId: Long? = null,
    val nextImageId: Long? = null
)
