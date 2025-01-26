package ca.apprajapati.composer.screens.drawing_demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/*
    Example to learn about Compose navigation to move from one screen to another.
    TODO: Type-safe navigation instead of strings.
 */

data class ScreensState(
    val screenChangeCount: Int = 0
)

class MainViewModel : ViewModel() {
    // Expose screen UI state
    private val _uiState = MutableStateFlow(ScreensState())

    fun consumableState() = _uiState.asStateFlow()

    fun increaseScreenChangeCount() {
        _uiState.update { currentState ->
            currentState.copy(
                screenChangeCount = currentState.screenChangeCount + 1
            )
        }
    }
}


@Composable
fun AnotherScreen(
    showOnlyPostsByUser: Boolean = false,
    navController: NavController,
    viewModel: MainViewModel,
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.DarkGray)
    {
        // LocalConfiguration.current works only inside a composable
        val configuration = LocalConfiguration.current

        val screenWidth = configuration.screenWidthDp
        val screenHeight = configuration.screenHeightDp

        Column(horizontalAlignment = Alignment.CenterHorizontally)
        {

            Spacer(modifier = Modifier.weight(0.5F))

            // Text had to be put inside a Box in order to center it inside the upper half of the screen.

            Box(
                modifier = Modifier.weight(0.5F),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    "Screen size (Dp): $screenWidth x $screenHeight",
                    fontSize = 24.sp, color = Color.LightGray

                )
            }

            Box(
                modifier = Modifier.weight(0.5F),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    "This is AnotherScreen, arg -> $showOnlyPostsByUser",
                    fontSize = 28.sp,
                    color = Color.LightGray
                )
            }

            Button(
                onClick =
                {
                    // When we started to use the navigateUp() method, the standard 'Back' button stopped to work for this app.

                    viewModel.increaseScreenChangeCount()
                    navController.navigateUp()
                },
                modifier = Modifier
                    .requiredHeight(100.dp)
                    .requiredWidth(200.dp)
                    .weight(1F),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            )
            {
                Text(text = "BACK", fontSize = 32.sp, color = Color.Blue)
            }

            Spacer(modifier = Modifier.weight(0.5F))
        }
    }
}


@Composable
fun MainScreen(
    navController: NavController,
    viewState: State<ScreensState>
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Black)
    {
        // LocalConfiguration.current works only inside a composable
        val configuration = LocalConfiguration.current

        val screenWidth = configuration.screenWidthDp
        val screenHeight = configuration.screenHeightDp

        Column(horizontalAlignment = Alignment.CenterHorizontally)
        {

            Spacer(modifier = Modifier.weight(0.5F))

            Box(
                modifier = Modifier.weight(0.5F),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    "Screen size (Dp): $screenWidth x $screenHeight",
                    fontSize = 24.sp, color = Color.LightGray

                )
            }


            Box(
                modifier = Modifier.weight(0.5F),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    "" + viewState.value.screenChangeCount + " SCREEN CHANGES",
                    fontSize = 24.sp, color = Color.LightGray

                )
            }

            Button(
                onClick =
                {
                    navController.navigate("AnotherScreen/true")
                },
                modifier = Modifier
                    .requiredHeight(100.dp)
                    .requiredWidth(200.dp)
                    .weight(1F),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan)
            )
            {
                Text(text = "CHANGE", fontSize = 32.sp, color = Color.Blue)
            }

            Spacer(modifier = Modifier.weight(0.5F))
        }

    }
}


@Composable
fun NavigationCompose(
    viewModel: MainViewModel
) {
    val navController = rememberNavController()

    val viewState = viewModel.consumableState().collectAsState()

    NavHost(
        navController = navController,
        startDestination = "MainScreen"
    ) {

        composable("MainScreen")
        {
            MainScreen(navController, viewState)
        }

        composable("AnotherScreen/{showOnlyPostsByUser}", arguments = listOf(
            navArgument("showOnlyPostsByUser") {
                type = NavType.BoolType
                defaultValue = false
            }
        )) {
            val showOnlyPostsByUser =
                it.arguments?.getBoolean("showOnlyPostsByUser") ?: false
            AnotherScreen(showOnlyPostsByUser, navController, viewModel)
        }
    }
}
