package bat.konst.kandinskyclient.ui.screens.requestScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        Row (
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { onNavigateTo(Route.NewRequest(state.md5)) }
        ) {
            // Конопка "Повторить"
            IconButton(
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp),
                onClick = { onNavigateTo(Route.NewRequest(state.md5)) }
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.rs_repeat)
                )
            }

            Column (
                modifier = Modifier
                    .padding(horizontal = 3.dp, vertical = 5.dp)
            ) {
                // Стиль
                Row {
                    Text(
                        text = stringResource(id = R.string.ms_style),
                        color = MaterialTheme.colorScheme.primary,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Light,
                        fontSize = 13.sp
                    )
                    Text(
                        text = state.style,
                        Modifier.padding(horizontal = 4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }

                // Промпт
                Text(
                    text = state.prompt,
                    color = MaterialTheme.colorScheme.primary,
                    lineHeight = 14.sp,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Отрицательный промпт
                Text(
                    color = Color.Red,
                    text = state.negativePrompt,
                    lineHeight = 14.sp,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }



        // images grid
        LazyVerticalGrid(
            columns = GridCells.Adaptive(90.dp)
        ) {
            items(state.images) { image ->
                Thumbinal(
                    status = image.status,
                    imageThumbnailBase64 = image.imageThumbnailBase64,
                    onClick = { onNavigateTo(Route.Image(id = image.id)) },
                    modifier = Modifier
                        .height(90.dp)
                        .padding(3.dp),
                    contentScale = ContentScale.FillHeight,
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