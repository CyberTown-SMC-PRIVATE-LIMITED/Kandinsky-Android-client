package bat.konst.kandinskyclient.ui.screens.newRequestScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import bat.konst.kandinskyclient.ui.navigation.Route
import bat.konst.kandinskyclient.R
import bat.konst.kandinskyclient.ui.screens.mainScreen.MainScreenEvent
import coil.compose.rememberAsyncImagePainter

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

    Column(
        modifier = Modifier
            .padding(16.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.nrs_add_request),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.padding(8.dp))


        // Промпт
        Text(
            text = stringResource(id = R.string.nrs_prompt),
            style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            maxLines = 4,
            minLines = 4,
            value = state.prompt,
            onValueChange = { onEvent(NewRequestScreenEvent.PromptUpdate(it)){} },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .verticalScroll(rememberScrollState()),
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Стиль
        Text(
            text = stringResource(id = R.string.nrs_style),
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 3.dp)
            ) {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextField(
                        // The `menuAnchor` modifier must be passed to the text field to handle
                        // expanding/collapsing the menu on click. A read-only text field has
                        // the anchor type `PrimaryNotEditable`.
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        value = state.style,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        // label = { Text(stringResource(id = R.string.nrs_style)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )
                    ExposedDropdownMenu(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        state.stylesList.forEach { style ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        style.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
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

            // Картинка стиля
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(start = 3.dp)
            ) {
                if (state.styleImageURL != "") {
                    Image(
                        painter = rememberAsyncImagePainter(state.styleImageURL),
                        contentDescription = "Style ${state.style} Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }
            }

        }


        Spacer(modifier = Modifier.padding(8.dp))

        // Негативный промпт
        Text(
            text = stringResource(id = R.string.nrs_negativeprompt),
            style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            maxLines = 5,
            minLines = 5,
            value = state.negativePrompt,
            onValueChange = { onEvent(NewRequestScreenEvent.NegativePromptUpdate(it)){} },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .verticalScroll(rememberScrollState()),
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // кнопки - выполнить
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 1..5) {
                Button(
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
                        Text(text = "+$i")
                }
            }
        }

    }
}


@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun NewRequestScreenPreview() {
    NewRequestView()
}
