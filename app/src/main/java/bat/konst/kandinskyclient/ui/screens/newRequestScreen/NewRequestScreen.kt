package bat.konst.kandinskyclient.ui.screens.newRequestScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import bat.konst.kandinskyclient.ui.navigation.Route
import bat.konst.kandinskyclient.R

@ExperimentalMaterial3Api
@Composable
fun NewRequestScreen(
    onNavigateTo: (Route) -> Unit = {},
) {
    val viewModel: NewRequestScreenViewModel = hiltViewModel()
    NewRequestView (
        onNavigateTo = onNavigateTo,
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}


@ExperimentalMaterial3Api
@Composable
fun NewRequestView(
    onNavigateTo: (Route) -> Unit = {},
    state: NewRequestScreenState = NewRequestScreenState(),
    onEvent: (NewRequestScreenEvent, onSuccess: () -> Unit) -> Unit = { _, _ -> }
) {
    // columnn with scrolling
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Промпт
        Text(
            text = stringResource(id = R.string.nrs_prompt),
            fontSize = 24.sp,
            modifier = Modifier
                .padding(top = 20.dp, bottom = 10.dp)
                .align(Alignment.Start)
        )
        TextField(
            maxLines = 5,
            minLines = 5,
            value = state.prompt,
            onValueChange = { onEvent(NewRequestScreenEvent.PromptUpdate(it)){} },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .verticalScroll(rememberScrollState()),
        )

        // Стиль
        Text(
            text = stringResource(id = R.string.nrs_style),
            fontSize = 24.sp,
            modifier = Modifier
                .padding(top = 20.dp, bottom = 10.dp)
                .align(Alignment.Start)
        )
        // TODO: Сделать нормальный список стилей
        val styleList = listOf("DEFAULT", "UHD", "ANIME", "KANDINSKY")
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier =Modifier
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
                label = { Text(stringResource(id = R.string.nrs_style)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                styleList.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                        onClick = {
                            onEvent(NewRequestScreenEvent.StyleUpdate(option)){}
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        // Негативный промпт
        Text(
            text = stringResource(id = R.string.nrs_negativeprompt),
            fontSize = 24.sp,
            modifier = Modifier
                .padding(top = 20.dp, bottom = 10.dp)
                .align(Alignment.Start)
        )
        TextField(
            maxLines = 5,
            minLines = 5,
            value = state.prompt,
            onValueChange = { onEvent(NewRequestScreenEvent.PromptUpdate(it)){} },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .verticalScroll(rememberScrollState()),
        )

        // кнопка - выполнить
        Button(
            onClick = {
                onEvent(NewRequestScreenEvent.AddRequest(prompt = state.prompt, negativePrompt = state.negativePrompt, style = state.style)){
                    onNavigateTo(Route.Main)
                }
            }
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Outlined.PlayArrow),
                contentDescription = null
            )
        }

    }
}


@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun NewRequestScreenPreview() {
    NewRequestView()
}
