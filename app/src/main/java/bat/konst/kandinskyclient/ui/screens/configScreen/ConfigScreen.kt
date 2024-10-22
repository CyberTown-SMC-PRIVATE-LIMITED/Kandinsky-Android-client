package bat.konst.kandinskyclient.ui.screens.configScreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun ConfigView(
    onNavigateTo: (Route) -> Unit = {},
    state: ConfigScreenState = ConfigScreenState(),
    onEvent: (ConfigScreenEvent) -> Unit = {},
) {
    val context = LocalContext.current

    // Событие на вход в экран
    var initialApiCalled by rememberSaveable { mutableStateOf(false) }
    if (!initialApiCalled) {
        LaunchedEffect(Unit) {
            initialApiCalled = true
            onEvent(ConfigScreenEvent.LoadConfig)
        }
    }

    // https://www.waseefakhtar.com/android/form-using-jetpack-compose-and-material-design/
    Column(
        modifier = Modifier
            .padding(16.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = stringResource(id = R.string.cs_kandinsky_keys),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // поле - ключ
        Text(
            text = stringResource(id = R.string.cs_key),
            style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.key,
            onValueChange = { onEvent(ConfigScreenEvent.KeyUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.cs_key_like)) },
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // поле - секрет
        Text(
            text = stringResource(id = R.string.cs_secret),
            style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.secret,
            onValueChange = { onEvent(ConfigScreenEvent.SecretUpdate(it)) },
            placeholder = { Text(text = stringResource(id = R.string.cs_secret_like)) },
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Кнопка сохранить конфигурацию
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                onEvent(ConfigScreenEvent.SaveConfig)
                onNavigateTo(Route.GoBack)
            },
        ) {
            Text(text = stringResource(id = R.string.cs_save_config))
        }

        Spacer(modifier = Modifier.padding(16.dp))

        // Кнопка - где взять?
        val url = stringResource(id = R.string.cs_kandinsky_url)
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                val urlIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )
                ContextCompat.startActivity(context, urlIntent, null)
            }
        ) {
            Text(
                text = stringResource(id = R.string.cs_kandinsky_keys_where)
            )
        }

    }
}

@Composable
@Preview(showBackground = true)
fun ConfigScreenPreview() {
    ConfigView()
}
