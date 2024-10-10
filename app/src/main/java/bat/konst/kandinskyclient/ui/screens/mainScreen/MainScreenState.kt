package bat.konst.kandinskyclient.ui.screens.mainScreen

sealed class MainScreenEvent {
    // примеры events
    data class EmailUpdate(val newEmail: String): MainScreenEvent()
    data class PasswordUpdate(val newPassword: String): MainScreenEvent()
}

data class MainScreenState(
    // примеры состояний
    val email: String = "",
    val password: String = ""
)
