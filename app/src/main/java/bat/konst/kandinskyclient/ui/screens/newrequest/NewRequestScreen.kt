package bat.konst.kandinskyclient.ui.screens.newrequest

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.ui.navigation.Route
import bat.konst.kandinskyclient.R
import bat.konst.kandinskyclient.app.AppState
import bat.konst.kandinskyclient.app.REQUEST_MIN_LENGTH
import bat.konst.kandinskyclient.ui.styles.textfields.TextFieldDropBox
import bat.konst.kandinskyclient.ui.styles.textfields.TextFieldPrompt
import bat.konst.kandinskyclient.ui.styles.text.TextDropBox
import bat.konst.kandinskyclient.ui.styles.text.TextH1
import bat.konst.kandinskyclient.ui.styles.text.TextH2
import coil.compose.rememberAsyncImagePainter
import bat.konst.kandinskyclient.ui.styles.icons.Dices

@ExperimentalMaterial3Api
@Composable
fun NewRequestScreen(
    onNavigateTo: (Route) -> Unit = {},
    route: Route.NewRequest = Route.NewRequest(md5="")
) {
    val viewModel: NewRequestScreenViewModel = hiltViewModel()
    NewRequestView (
        onNavigateTo = onNavigateTo,
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        route = route
    )
}


@ExperimentalMaterial3Api
@Composable
fun NewRequestView(
    onNavigateTo: (Route) -> Unit = {},
    state: NewRequestScreenState = NewRequestScreenState(),
    onEvent: (NewRequestScreenEvent, onSuccess: () -> Unit) -> Unit = { _, _ -> },
    route: Route.NewRequest = Route.NewRequest(md5="")
) {
    // Событие на вход в экран
    var initialApiCalled by rememberSaveable { mutableStateOf(false) }
    if (!initialApiCalled) {
        LaunchedEffect(Unit) {
            initialApiCalled = true
            onEvent(NewRequestScreenEvent.ScreenUpdate(route.md5)){}
        }
    }

    Box( // Box вокруг column -- чтобы работал scroll даже вне формы
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 550.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            // horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextH1(text = stringResource(id = R.string.nrs_add_request))
                Icon(
                    imageVector = Dices,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { onEvent(NewRequestScreenEvent.RollRequest){} }
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))


            // Промпт
            TextH2(text = stringResource(id = R.string.nrs_prompt))
            TextFieldPrompt(
                value = state.prompt,
                onValueChange = { onEvent(NewRequestScreenEvent.PromptUpdate(it)) {} }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            // Стиль
            TextH2(text = stringResource(id = R.string.nrs_style))


            // Выбор стиля
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextFieldDropBox(
                        value = state.style,
                        styleImageURL = state.styleImageURL,
                        expanded = expanded,
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        AppState.stylesList.forEach { style ->
                            DropdownMenuItem(
                                text = {
                                    TextDropBox(style.name)
                                },
                                onClick = {
                                    onEvent(NewRequestScreenEvent.StyleUpdate(style.name)) {}
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.padding(8.dp))

            // Негативный промпт
            TextH2(text = stringResource(id = R.string.nrs_negativeprompt))
            TextFieldPrompt(
                value = state.negativePrompt,
                onValueChange = { onEvent(NewRequestScreenEvent.NegativePromptUpdate(it)) {} }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            // кнопки - выполнить
            TextH2(text = stringResource(id = R.string.nrs_qw))
            Row(
                modifier = Modifier
                    .align(Alignment.Start)
                // horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val istart = 1
                val iend = 5
                val istartShape = RoundedCornerShape(30, 5, 5, 30)
                val iendShape = RoundedCornerShape(5, 30, 30, 5)
                val icenterShape = RoundedCornerShape(5, 5, 5, 5)
                for (i in istart..iend) {
                    Button(
                        contentPadding = PaddingValues(0.dp),
                        shape = when (i) {
                            istart -> istartShape
                            iend -> iendShape
                            else -> icenterShape
                        },
                        enabled = state.prompt.trim().length >= REQUEST_MIN_LENGTH,
                        onClick = {
                            onEvent(
                                NewRequestScreenEvent.AddRequest(
                                    prompt = state.prompt,
                                    negativePrompt = state.negativePrompt,
                                    style = state.style,
                                    qw = i
                                )
                            ) {
                                onNavigateTo(Route.GoBack)
                            }
                        }
                    ) {
                        Row {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                            Text(text = i.toString())
                        }
                    }

                    if (i != iend) {
                        Spacer(modifier = Modifier.width(0.3.dp))
                    }
                }
            }

        }
    }
}

@Composable
fun StyleImage(imageUrl: String) {
    // картинка стиля
    if (imageUrl != "") {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .height(43.dp)
                .clip(RoundedCornerShape(20)),
        )
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun NewRequestScreenPreview() {
    NewRequestView()
}
