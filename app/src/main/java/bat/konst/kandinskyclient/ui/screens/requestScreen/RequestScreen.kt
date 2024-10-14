package bat.konst.kandinskyclient.ui.screens.requestScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.ui.navigation.Route
import bat.konst.kandinskyclient.ui.screens.configScreen.ConfigScreenEvent
import bat.konst.kandinskyclient.ui.screens.configScreen.ConfigScreenState

@Composable
fun RequestScreen(
    onNavigateTo: (Route) -> Unit = {},
    route: Route.Request = Route.Request(md5="")
) {
    val viewModel: RequestScreenViewModel = hiltViewModel()
    RequestView(
        onNavigateTo = onNavigateTo,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        route = route
    )
}

@Composable
fun RequestView(
    onNavigateTo: (Route) -> Unit = {},
    state: RequestScreenState = RequestScreenState(),
    onEvent: (RequestScreenEvent) -> Unit = {},
    route: Route.Request = Route.Request(md5="")
) {
    Column {

        // Событие на вход в экран
        LaunchedEffect(key1 = state.openKey) {
            onEvent(RequestScreenEvent.ScreenUpdate(route.md5))
        }

        Text(text = "Request: ${route.md5}")

    }
}