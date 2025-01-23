package ca.apprajapati.composer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.apprajapati.composer.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {


                    }
                }
            }
        }
    }
}


@Composable
fun DrawingDemo(){
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = Modifier.fillMaxSize()) {

        val height = size.height
        val width = size.width

        drawText(textMeasurer = textMeasurer,
            text = "Ajay",
            topLeft = Offset(x = 20f, y = 40f),
            style = TextStyle(fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        )

        drawLine(color = Color.Green,
            start = Offset( x = screenWidth / 6f, y = screenHeight / 6f),
            end = Offset( x = (screenWidth / 6f) * 15f, y = screenHeight / 6f),
            strokeWidth = 8f
        )

        val rectSize = 300f
        drawRect(
            color= Color.Cyan, 
            topLeft =
                Offset(
                    x = width/2f - rectSize/2f, y = height/2f - rectSize/2f - height / 3f),
            size = Size(rectSize, rectSize)
        )

        val outerRect = 320f
        drawRect(
            color= Color.Blue,
            topLeft =
            Offset(
                x = width/2f - outerRect/2f, y = height/2f - outerRect/2f - height / 3f),
            size = Size(outerRect, outerRect),
            style = Stroke(width = 4f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DrawingDemoPreview(){
    DrawingDemo()
}
