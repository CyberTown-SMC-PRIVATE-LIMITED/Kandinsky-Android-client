package bat.konst.kandinskyclient.ui.screens.imageScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.ui.navigation.Route
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
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


    }
}

@Composable
@Preview(showBackground = true)
fun ImageScreenPreview() {
    ImageView()
}