package bat.konst.kandinskyclient.ui.screens.newrequest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bat.konst.kandinskyclient.ui.navigation.Route
import bat.konst.kandinskyclient.R
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
            maxLines = 4,
            minLines = 4,
            value = state.negativePrompt,
            onValueChange = { onEvent(NewRequestScreenEvent.NegativePromptUpdate(it)){} },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .verticalScroll(rememberScrollState()),
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // кнопки - выполнить
        Text(
            text = stringResource(id = R.string.nrs_qw),
            style = MaterialTheme.typography.bodyLarge
        )
        Row (
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


@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun NewRequestScreenPreview() {
    NewRequestView()
}
