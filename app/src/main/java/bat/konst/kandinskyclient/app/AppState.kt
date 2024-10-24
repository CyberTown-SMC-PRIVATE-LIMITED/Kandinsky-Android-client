package bat.konst.kandinskyclient.app

import kotlinx.coroutines.flow.MutableStateFlow


object AppState {
    // Используется дла обновления экрана
    // Изменяется при изменении данных DB в Worker
    var RoomDataChaged = MutableStateFlow(0L)
}