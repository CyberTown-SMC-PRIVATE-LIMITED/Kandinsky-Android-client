package bat.konst.kandinskyclient.ui.screens.imageScreen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.ui.navigation.Route
import coil.compose.AsyncImage
import bat.konst.kandinskyclient.R
import java.io.File

@Composable
fun ImageScreen(
    onNavigateTo: (Route) -> Unit = {},
    route: Route.Image = Route.Image(id=0)
) {
    val viewModel: ImageScreenViewModel = hiltViewModel()
    ImageView (
        onNavigateTo = onNavigateTo,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        route = route,
    )
}

@Composable
fun ImageView(
    onNavigateTo: (Route) -> Unit = {},
    state: ImageScreenState = ImageScreenState(),
    onEvent: (ImageScreenEvent) -> Unit = {},
    route: Route.Image = Route.Image(id=0)
) {

    // Событие на вход в экран
    LaunchedEffect(key1 = state.openKey) {
        onEvent(ImageScreenEvent.ScreenUpdate(route.id))
    }

    Column {

        Text(text = "image id = ${state.id}")
        Text(text = "image status = ${state.status}")
        Text(text = "imageBase64  = ${state.imageBase64}")
        Text(text = "OK")
        AsyncImage(
            model = File(state.imageBase64),
            contentDescription = "generated image"
        )
        Text(text = "OK")

        Row {
            // Кнопка "Назад"
            if (state.prevImageId != null) {
                Button(
                    onClick = { onNavigateTo(Route.Image(state.prevImageId)) },
                    modifier = Modifier
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                }
            }

            // Кнопка "Поделиться"
            Share(filePath = state.imageBase64, context = LocalContext.current)


            // Кнопка "Вперед"
            if (state.nextImageId != null) {
                Button(
                    onClick = { onNavigateTo(Route.Image(state.nextImageId)) },
                    modifier = Modifier
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
            }
        }

    }
}

// Кнопка "Поделиться"
@Composable
fun Share(filePath: String, context: Context) {
    Button(onClick = {
        // https://readmedium.com/android-kotlin-jetpack-compose-file-sharing-using-fileprovider-and-android-sharesheet-3b8a7b9fb82d
        // Ко всему этому нужно provider (разрешение) в Manifest
        // и добавить в res/xml filepaths.xml !!!!

        try {
            val imageFile = File(filePath)
            val uri = FileProvider.getUriForFile(
                context,
                "bat.konst.kandinskyclient.fileprovider",
                imageFile
            )
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/jpeg"
            }
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val shareIntent = Intent.createChooser(sendIntent, null)
            ContextCompat.startActivity(context, shareIntent, null)
        } catch (e: Exception) {
            Log.d("KandinskyClient", e.toString())
        }
    }) {
        Icon(imageVector = Icons.Default.Share, contentDescription = null)
        Text(text = stringResource(id = R.string.ps_share), modifier = Modifier.padding(start = 8.dp))
    }
}


@Composable
@Preview(showBackground = true)
fun ImageScreenPreview() {
    ImageView()
}