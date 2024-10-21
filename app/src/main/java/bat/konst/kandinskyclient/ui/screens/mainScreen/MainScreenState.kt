package bat.konst.kandinskyclient.ui.screens.mainScreen

import bat.konst.kandinskyclient.data.room.entity.Request
import bat.konst.kandinskyclient.data.room.entity.RequestJoinImage

sealed class MainScreenEvent {
    data object ScreenUpdate: MainScreenEvent()
}

data class MainScreenState(
    val requests: List<RequestJoinImage> = emptyList<RequestJoinImage>(),
    val openKey: Long = 0 // ключ для запуска события открытия экрана
)
