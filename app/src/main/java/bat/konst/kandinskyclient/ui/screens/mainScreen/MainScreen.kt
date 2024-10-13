package bat.konst.kandinskyclient.ui.screens.mainScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import bat.konst.kandinskyclient.R
import bat.konst.kandinskyclient.ui.navigation.Route
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.data.room.entity.Request

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
fun MainView(
    onNavigateTo: (Route) -> Unit = {},
    state: MainScreenState = MainScreenState(),
    onEvent: (MainScreenEvent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {

            // кнопка "Обновить"
            Button(
                onClick = { onEvent(MainScreenEvent.ScreenUpdate) },
                modifier = Modifier
            ) {
                Text(text = stringResource(id = R.string.ms_refresh))
            }

            // Кнопка "Добавить"
            Button(
                onClick = { onNavigateTo(Route.NewRequest) },
                modifier = Modifier
            ) {
                Text(text = stringResource(id = R.string.ms_add))
            }

        }

        // Список запросов
        LazyColumn {
            items(state.requests) { request ->
                RequestCard(request)
            }
        }

    }
}

@Composable
fun RequestCard(request: Request) {
    Card(
        modifier= Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Column {
            Text(text = request.prompt)
            Text(text = request.style)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    MainView(state = MainScreenState(
        requests = listOf(
            Request(
                md5 = "some_md5",
                prompt = "Промт запроса",
                negativePrompt = "Негативный промт",
                style = "DEFAULT",
                dateCreate = 0,
                dateUpdate = 0
            ),
            Request(
                md5 = "some_md5_2",
                prompt = "Промт запроса 2",
                negativePrompt = "Негативный промт 2",
                style = "DEFAULT",
                dateCreate = 0,
                dateUpdate = 0
            )
        )
    )
    )
}
