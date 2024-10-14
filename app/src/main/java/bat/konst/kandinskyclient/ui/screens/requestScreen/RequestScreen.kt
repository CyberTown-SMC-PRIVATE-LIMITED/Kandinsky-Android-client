package bat.konst.kandinskyclient.ui.screens.requestScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.data.room.entity.Image
import bat.konst.kandinskyclient.ui.navigation.Route
import bat.konst.kandinskyclient.ui.screens.configScreen.ConfigScreenEvent
import bat.konst.kandinskyclient.ui.screens.configScreen.ConfigScreenState
import bat.konst.kandinskyclient.ui.screens.mainScreen.RequestCard

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
    // Событие на вход в экран
    LaunchedEffect(key1 = state.openKey) {
        onEvent(RequestScreenEvent.ScreenUpdate(route.md5))
    }

    Column {

        // промпты
        Card {
            Column {
                Text(text = state.prompt)
                Text(text = state.negativePrompt)
                Text(text = state.style)
            }
        }

        // images
        LazyColumn {
            items(state.images) { image ->
                Text(text = "image id = ${image.id}")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun RequestScreenPreview() {
    RequestView(
        state = RequestScreenState(
            prompt = "Some generation prompt",
            negativePrompt = "- negative -",
            style = "ANIME",
            images = listOf(
                Image(
                    id=1,
                    md5="some_md5",
                    kandinskyId = "knd_sky_id",
                    status = 0,
                    dateCreated = 0,
                    imageBase64 = ""
                ),
                Image(
                    id=2,
                    md5="some_md5",
                    kandinskyId = "knd_sky_id2",
                    status = 0,
                    dateCreated = 0,
                    imageBase64 = ""
                )
            )
        )
    )
}