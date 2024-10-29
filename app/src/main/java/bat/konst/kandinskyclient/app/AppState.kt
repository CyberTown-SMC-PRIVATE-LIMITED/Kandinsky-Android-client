package bat.konst.kandinskyclient.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import bat.konst.kandinskyclient.data.kandinskyApi.models.Styles
import kotlinx.coroutines.flow.MutableStateFlow


object AppState {
    // тема приложения
    var isDatkTheme by mutableStateOf(false)
    // Используется дла обновления экрана
    // Изменяется при изменении данных DB в Worker
    var RoomDataChaged = MutableStateFlow(0L)
    // нужно ли пытаться показать окно конфига если ключи не заполнены
    var needCheckConfig by mutableStateOf(true)
    // список стилей из API Кандинского
    var stylesList: Styles = Styles()
}