package bat.konst.kandinskyclient.ui.screens.configScreen

sealed class ConfigScreenEvent {
    data class KeyUpdate(val newKey: String): ConfigScreenEvent()
    data class SecretUpdate(val newSecret: String): ConfigScreenEvent()
    data object SaveConfig: ConfigScreenEvent()
    data object LoadConfig: ConfigScreenEvent()
}

data class ConfigScreenState(
    val key: String = "",
    val secret: String = "",
)
