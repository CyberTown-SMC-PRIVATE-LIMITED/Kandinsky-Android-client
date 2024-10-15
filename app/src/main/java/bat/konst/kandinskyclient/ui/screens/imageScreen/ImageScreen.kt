package bat.konst.kandinskyclient.ui.screens.imageScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.ui.navigation.Route

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

    }
}

@Composable
@Preview(showBackground = true)
fun ImageScreenPreview() {
    ImageView()
}