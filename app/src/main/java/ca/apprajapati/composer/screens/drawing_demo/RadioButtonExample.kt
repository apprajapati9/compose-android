package ca.apprajapati.composer.screens.drawing_demo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun KindRadioGroup(
    mItems: List<String>,
    selected: String,
    setSelected: (selected: String) -> Unit,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            mItems.forEach { item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected == item,
                        onClick = {
                            setSelected(item)
                        },
                        enabled = true,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.Magenta
                        )
                    )

                    // Using requiredWidth() for text made it possible to align the
                    // items nicely also when the texts have varying lengths.

                    Text(
                        text = item,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .requiredWidth(100.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun RadioButtonExample() {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val kinds = listOf("Square", "Ball", "Rectangle")
    val (selected, setSelected) = remember { mutableStateOf("Square") }

    Column(horizontalAlignment = Alignment.CenterHorizontally)
    {
        Box(
            modifier = Modifier
                .requiredWidth(screenWidth)
                .weight(8F)
        )
        {
            Canvas(
                modifier = Modifier.fillMaxSize()
            )
            {
                val canvasWidth = size.width
                val canvasHeight = size.height

                when (selected) {
                    kinds[0] -> {
                        drawRect(
                            color = Color.Cyan,
                            topLeft = Offset(x = canvasWidth / 4F, y = canvasHeight / 4F),
                            size = Size(canvasWidth / 2F, canvasWidth / 2F)
                        )

                        drawRect(
                            color = Color.Black,
                            topLeft = Offset(x = canvasWidth / 4F, y = canvasHeight / 4F),
                            size = Size(canvasWidth / 2F, canvasWidth / 2F),
                            style = Stroke(width = 4F)
                        )
                    }
                    kinds[1] -> {
                        drawCircle(
                            color = Color.Yellow,
                            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                            radius = size.minDimension / 4
                        )

                        drawCircle(
                            color = Color.Black,
                            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
                            radius = size.minDimension / 4,
                            style = Stroke(width = 4F)
                        )
                    }
                    else -> {
                        drawRect(
                            color = Color.Blue,
                            topLeft = Offset(x = canvasWidth / 6F, y = canvasHeight / 4F),
                            size = Size(canvasWidth / 1.5F, canvasWidth / 2F),
                            style = Stroke(width = 12F)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .requiredWidth(screenWidth)
                .weight(6F)
        )
        {
            Column {
                KindRadioGroup(
                    mItems = kinds,
                    selected, setSelected
                )
                Text(
                    text = "Selected Option : $selected",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

    }
}




