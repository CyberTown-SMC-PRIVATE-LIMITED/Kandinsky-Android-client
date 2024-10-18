package bat.konst.kandinskyclient.ui.screens.mainScreen

import bat.konst.kandinskyclient.data.room.entity.Request

sealed class MainScreenEvent {
    data object ScreenUpdate: MainScreenEvent()
}

data class MainScreenState(
    val requests: List<Request> = emptyList<Request>(),
    val openKey: Long = 0 // ключ для запуска события открытия экрана
)
