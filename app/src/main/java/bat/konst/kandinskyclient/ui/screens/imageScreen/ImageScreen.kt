package bat.konst.kandinskyclient.ui.screens.imageScreen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import bat.konst.kandinskyclient.ui.screens.mainScreen.MainScreenEvent
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import java.io.File
import kotlin.system.exitProcess

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

    Scaffold(

        floatingActionButton = {
            // Кнопка "Поделиться"
            Share(filePath = state.imageBase64, context = LocalContext.current)
        }

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // фоновое изображение
            AsyncImage(
                model = File(state.imageBase64),
                contentDescription = "generated image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = 26.dp)
            )

            // Изображение
            AsyncImage(
                model = File(state.imageBase64),
                contentDescription = "generated image",
                Modifier.fillMaxSize()
            )


            Row (
                modifier = Modifier
                    //.align(Alignment.CenterVertically)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Кнопка "Вперед"

                Button(
                    onClick = { onEvent(ImageScreenEvent.ScreenUpdate(state.nextImageId!!)) },
                    shape = CircleShape,
                    enabled = state.nextImageId != null,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }


                // Кнопка "Назад"
                Button(
                    onClick = { onEvent(ImageScreenEvent.ScreenUpdate(state.prevImageId!!)) },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray.copy(alpha = 0.7f)),
                    enabled = state.prevImageId != null,
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null
                    )
                }

            }

        }
    }
}

fun crossfade(b: Boolean) {

}

// Кнопка "Поделиться"
@Composable
fun Share(filePath: String, context: Context) {
    FloatingActionButton(
        shape = CircleShape,
        onClick = {
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
        Icon(imageVector = Icons.Default.Share, contentDescription = stringResource(id = R.string.ps_share))
    }
}


@Composable
@Preview(showBackground = true)
fun ImageScreenPreview() {
    ImageView()
}