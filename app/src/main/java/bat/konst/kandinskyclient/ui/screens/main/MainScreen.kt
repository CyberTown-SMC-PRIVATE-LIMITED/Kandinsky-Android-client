package bat.konst.kandinskyclient.ui.screens.main

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import bat.konst.kandinskyclient.R
import bat.konst.kandinskyclient.ui.navigation.Route
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.core.content.ContextCompat.startActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import bat.konst.kandinskyclient.app.AppState
import bat.konst.kandinskyclient.data.room.entity.RequestJoinImage
import bat.konst.kandinskyclient.ui.components.requestcard.RequestCard
import bat.konst.kandinskyclient.ui.components.swipeaction.SwipeToDismissListItem


@Composable
fun MainScreen(
    onNavigateTo: (Route) -> Unit = {},
) {
    val viewModel: MainScreenViewModel = hiltViewModel()
    MainView (
        onNavigateTo = onNavigateTo,
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun MainView(
    onNavigateTo: (Route) -> Unit = {},
    state: MainScreenState = MainScreenState(),
    onEvent: (MainScreenEvent) -> Unit = {}
) {

    // Событие на вход в экран и изменение в БД
    LaunchedEffect(key1 = AppState.RoomDataChaged.collectAsState().value) {
        onEvent(MainScreenEvent.ScreenUpdate)
    }

    // По нажатию системной кнопки "назад" свернуть приложение
    val context = LocalContext.current
    BackHandler {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(context, intent, null)
    }

    Scaffold (
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(0.dp)) {  }
        },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 20.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    color = MaterialTheme.colorScheme.primary
                )

                // Кнопка "Настройки"
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.Settings,
                    contentDescription = stringResource(id = R.string.ms_settings),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { onNavigateTo(Route.Config) }
                )
            }
        },

        floatingActionButton = {
            // Кнопка "Добавить"
            FloatingActionButton(
                shape = CircleShape,
                onClick = { onNavigateTo(Route.NewRequest()) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.ms_add))
            }
        }

    ) { innerPadding ->

        // Список запросов
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(state.requests) { request ->
                SwipeToDismissListItem(
                    onStartToEnd = {
                        // Запрос на добавление картинок
                        onNavigateTo(Route.NewRequest(request.md5))
                    },
                    onEndToStart = {
                        onNavigateTo(Route.Request(request.md5))
                    },
                ) {
                    RequestCard(request, onNavigateTo)
                }
            }
        }

    }

}

@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    MainView(state = MainScreenState(
        requests = listOf(
            RequestJoinImage(
                md5 = "",
                prompt = "Промт запроса",
                negativePrompt = "Негативный промт",
                style = "DEFAULT",
                status = 0,
                imageThumbnailBase64 = ""
            ),
            RequestJoinImage(
                md5 = "",
                prompt = "Промт запроса 2",
                negativePrompt = "Негативный промт 2",
                style = "DEFAULT",
                status = 0,
                imageThumbnailBase64 = ""
            )
        )
    ))
}
