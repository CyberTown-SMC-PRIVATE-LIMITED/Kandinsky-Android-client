package bat.konst.kandinskyclient.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import bat.konst.kandinskyclient.ui.screens.newRequestScreen.NewRequestScreen
import bat.konst.kandinskyclient.ui.screens.mainScreen.MainScreen
import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable data object GoBack: Route()  // роут для возврата к предыдущему экрану
    @Serializable data object Main: Route()
    @Serializable data object NewRequest: Route()
    // @Serializable data class ShowImage(val id: Int): Route()
}

@ExperimentalMaterial3Api
@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {

    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Route.NewRequest
    ) {
        val navigateAction: (Route) -> Unit = { navigateTo ->
            when (navigateTo) {
                is Route.GoBack -> navHostController.navigateUp()
                else -> navHostController.navigate(navigateTo)
            }
        }

        composable<Route.Main> {
            MainScreen(onNavigateTo = navigateAction)
        }

        composable<Route.NewRequest> {
            NewRequestScreen(onNavigateTo = navigateAction)
        }


//        composable<Route.ShowImage> { backStackEntry ->
//            // ShowImageScreen(onNavigateTo = navigateAction, colors = backStackEntry.toRoute())
//        }
    }
}
