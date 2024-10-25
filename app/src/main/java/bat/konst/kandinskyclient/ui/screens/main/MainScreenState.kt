package bat.konst.kandinskyclient.ui.screens.main

import bat.konst.kandinskyclient.data.room.entity.RequestJoinImage

sealed class MainScreenEvent {
    data object ScreenUpdate: MainScreenEvent()
    data class RequestDelete(val md5: String): MainScreenEvent()
}

data class MainScreenState(
    val requests: List<RequestJoinImage> = emptyList<RequestJoinImage>(),
)
