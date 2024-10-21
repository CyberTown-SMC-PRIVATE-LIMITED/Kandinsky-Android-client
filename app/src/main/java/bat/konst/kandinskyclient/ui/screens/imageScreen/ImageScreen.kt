package bat.konst.kandinskyclient.ui.screens.imageScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.ui.navigation.Route
import coil.compose.AsyncImage
import bat.konst.kandinskyclient.ui.components.shareButton.ShareButton
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
@OptIn(ExperimentalMaterial3Api::class)
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
        topBar = {
            TopAppBar(title = {}, modifier = Modifier.height(0.dp))
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(0.dp)) {  }
        },


        floatingActionButton = {
            // Кнопка "Поделиться"
            ShareButton(filePath = state.imageBase64, context = LocalContext.current)
        }

    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // фоновое изображение
            AsyncImage(
                model = File(state.imageBase64),
                contentDescription = "generated image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = 26.dp)
                    .alpha(0.7f)
            )

            // Изображение
            AsyncImage(
                model = File(state.imageBase64),
                contentDescription = "generated image",
                Modifier.fillMaxSize()
            )


            // кнопки - назад/вперёд
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

@Composable
@Preview(showBackground = true)
fun ImageScreenPreview() {
    ImageView()
}