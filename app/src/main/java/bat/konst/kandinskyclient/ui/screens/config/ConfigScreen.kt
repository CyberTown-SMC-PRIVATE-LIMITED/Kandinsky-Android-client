package bat.konst.kandinskyclient.ui.screens.config

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.ui.navigation.Route
import bat.konst.kandinskyclient.R
import kotlinx.coroutines.launch

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
    onEvent: (ConfigScreenEvent, onSuccess: () -> Unit, onError: () -> Unit) -> Unit = { _, _, _ -> },
) {
    val context = LocalContext.current

    // Событие на вход в экран
    var initialApiCalled by rememberSaveable { mutableStateOf(false) }
    if (!initialApiCalled) {
        LaunchedEffect(Unit) {
            initialApiCalled = true
            onEvent(ConfigScreenEvent.LoadConfig, {}, {})
        }
    }

    Box( // Box вокруг column -- чтобы работал scroll даже вне формы
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 550.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            // horizontalAlignment = Alignment.CenterHorizontally
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
                onValueChange = { onEvent(ConfigScreenEvent.KeyUpdate(it), {}, {}) },
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
                onValueChange = { onEvent(ConfigScreenEvent.SecretUpdate(it), {}, {}) },
                placeholder = { Text(text = stringResource(id = R.string.cs_secret_like)) },
            )

            Spacer(modifier = Modifier.padding(8.dp))

            // Кнопка сохранить конфигурацию
            val scope = rememberCoroutineScope() // для snackbar - всплывающего сообщения
            val snackbarHostState = remember { SnackbarHostState() }
            SnackbarHost(hostState = snackbarHostState)
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.CenterHorizontally),
                enabled = state.secret.trim() != "" && state.key.trim() != "",
                onClick = {
                    onEvent(
                        ConfigScreenEvent.SaveConfig,
                        { onNavigateTo(Route.GoBack) }, // onSuccess
                        {  // onError
                            scope.launch {
                                snackbarHostState.showSnackbar("Failed to save config")
                            }
                        }
                    )
                },
            ) {
                Text(text = stringResource(id = R.string.cs_save_config))
            }

            Spacer(modifier = Modifier.padding(26.dp))

            // Кнопка - где взять?
            val url = stringResource(id = R.string.cs_kandinsky_keys_where_url)
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

            // Кнопка - как взять?
            val urlHow = stringResource(id = R.string.cs_kandinsky_keys_how_url)
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    val urlIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(urlHow)
                    )
                    ContextCompat.startActivity(context, urlIntent, null)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.cs_kandinsky_keys_how)
                )
            }


        }
    }
}

@Composable
@Preview(showBackground = true)
fun ConfigScreenPreview() {
    ConfigView()
}
