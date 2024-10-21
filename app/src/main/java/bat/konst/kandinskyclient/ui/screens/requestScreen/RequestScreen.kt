package bat.konst.kandinskyclient.ui.screens.requestScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.R
import bat.konst.kandinskyclient.data.room.entity.Image
import bat.konst.kandinskyclient.data.room.entity.StatusTypes
import bat.konst.kandinskyclient.ui.components.thumbinal.Thumbinal
import bat.konst.kandinskyclient.ui.navigation.Route
import coil.compose.AsyncImage
import java.io.File

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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
        ) {
            Column {
                Text(text = state.prompt)
                Text(text = state.negativePrompt)
                Text(text = state.style)
            }
        }

        // Конопка "Повторить"
        Button(
            onClick = { onNavigateTo(Route.NewRequest(state.md5)) }
        ) {
            Text(text = stringResource(id = R.string.rs_repeat))
        }

        // images grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            items(state.images) { image ->
                Thumbinal(
                    status = image.status,
                    imageThumbnailBase64 = image.imageThumbnailBase64,
                    onClick = { onNavigateTo(Route.Image(id = image.id)) },
                    modifier = Modifier
                        .padding(3.dp)
                )
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
                    imageBase64 = "",
                    imageThumbnailBase64 = ""
                ),
                Image(
                    id=2,
                    md5="some_md5",
                    kandinskyId = "knd_sky_id2",
                    status = 0,
                    dateCreated = 0,
                    imageBase64 = "",
                    imageThumbnailBase64 = ""
                )
            )
        )
    )
}