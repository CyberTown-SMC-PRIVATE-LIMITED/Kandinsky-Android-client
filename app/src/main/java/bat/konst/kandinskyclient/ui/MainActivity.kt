package bat.konst.kandinskyclient.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import bat.konst.kandinskyclient.ui.navigation.MainNavigation
import bat.konst.kandinskyclient.ui.theme.KandinskyClientTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KandinskyClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ContentNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ContentNavigation(
    modifier: Modifier = Modifier,
) {
    MainNavigation(
        navHostController = rememberNavController(),
        modifier = modifier
    )
}


@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KandinskyClientTheme {
        ContentNavigation()
    }
}