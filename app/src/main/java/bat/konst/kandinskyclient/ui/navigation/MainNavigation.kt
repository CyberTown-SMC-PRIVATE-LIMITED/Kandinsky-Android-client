package bat.konst.kandinskyclient.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import bat.konst.kandinskyclient.ui.screens.config.ConfigScreen
import bat.konst.kandinskyclient.ui.screens.image.ImageScreen
import bat.konst.kandinskyclient.ui.screens.newrequest.NewRequestScreen
import bat.konst.kandinskyclient.ui.screens.main.MainScreen
import bat.konst.kandinskyclient.ui.screens.request.RequestScreen
import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable data object GoBack: Route()  // роут для возврата к предыдущему экрану
    @Serializable data object Main: Route()
    @Serializable data class NewRequest(val md5: String = ""): Route()
    @Serializable data object Config: Route()
    @Serializable data class Request(val md5: String): Route()
    @Serializable data class Image(val id: Long): Route()
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
        startDestination = Route.Main
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

        composable<Route.NewRequest> { backStackEntry ->
            NewRequestScreen(onNavigateTo = navigateAction, route = backStackEntry.toRoute())
        }

        composable<Route.Config> {
            ConfigScreen(onNavigateTo = navigateAction)
        }

        composable<Route.Request> { backStackEntry ->
            RequestScreen(onNavigateTo = navigateAction, route = backStackEntry.toRoute())
        }

        composable<Route.Image> { backStackEntry ->
            ImageScreen(onNavigateTo = navigateAction, route = backStackEntry.toRoute())
        }

    }
}
