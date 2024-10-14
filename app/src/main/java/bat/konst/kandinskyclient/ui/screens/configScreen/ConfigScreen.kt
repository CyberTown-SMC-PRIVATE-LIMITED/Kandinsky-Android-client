package bat.konst.kandinskyclient.ui.screens.configScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.ui.navigation.Route
import bat.konst.kandinskyclient.R

@Composable
fun ConfigScreen(
    onNavigateTo: (Route) -> Unit = {},
) {
    val viewModel: ConfigScreenViewModel = hiltViewModel()
    ConfigView(
        onNavigateTo = onNavigateTo,
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ConfigView(
    onNavigateTo: (Route) -> Unit = {},
    state: ConfigScreenState = ConfigScreenState(),
    onEvent: (ConfigScreenEvent) -> Unit = {}
) {
    Column() {

        // Кнопка прочитать конфигурацию
        Button(
            onClick = { onEvent(ConfigScreenEvent.LoadConfig) },
        ) {
            Text(text = stringResource(id = R.string.cs_load_config))
        }

        // поле - ключ
        TextField(
            value = state.key,
            onValueChange = { onEvent(ConfigScreenEvent.KeyUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.cs_key)) },
        )

        // поле - секрет
        TextField(
            value = state.secret,
            onValueChange = { onEvent(ConfigScreenEvent.SecretUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.cs_secret)) },
        )

        // Кнопка сохранить конфигурацию
        Button(
            onClick = { onEvent(ConfigScreenEvent.SaveConfig) },
        ) {
            Text(text = stringResource(id = R.string.cs_save_config))
        }

    }
}

@Composable
@Preview(showBackground = true)
fun ConfigScreenPreview() {
    ConfigView()
}
