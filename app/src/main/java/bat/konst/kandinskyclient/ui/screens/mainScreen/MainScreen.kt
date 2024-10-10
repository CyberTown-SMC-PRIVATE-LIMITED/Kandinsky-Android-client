package bat.konst.kandinskyclient.ui.screens.mainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import bat.konst.kandinskyclient.ui.navigation.Route

@Composable
fun MainScreen(
    onNavigateTo: (Route) -> Unit = {},
) {
    val viewModel: MainScreenViewModel = viewModel()
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


    }
}


@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    MainView()
}
