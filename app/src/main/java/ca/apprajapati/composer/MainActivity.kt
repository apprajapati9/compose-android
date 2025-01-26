package ca.apprajapati.composer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ca.apprajapati.composer.screens.drawing_demo.MainViewModel
import ca.apprajapati.composer.screens.drawing_demo.NavigationCompose
import ca.apprajapati.composer.snake_game.SnakeScreen
import ca.apprajapati.composer.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // DrawingDemo()
                        // MovingBall()
                        //RadioButtonExample()
                        //NavigationCompose(viewModel)
                        SnakeScreen(10f)
                    }
                }
            }
        }
    }
}
