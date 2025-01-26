package ca.apprajapati.composer.screens.drawing_demo


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BallControlButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String
    //content: @Composable RowScope.() -> Unit
) {
    Button(
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        modifier = modifier
    )
    {
        Text(text = buttonText, fontSize = 36.sp, color = Color.White)
    }
}

@Composable
fun MovingBall() {
    val configuration = LocalConfiguration.current

    // val context = LocalContext.current // to get the context

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val ballOffsetX = remember { mutableFloatStateOf(0F) }
    val ballOffsetY = remember { mutableFloatStateOf(0F) }

    val ballColor = remember { mutableStateOf(Color.Red) }

    val menuShowOnOff = remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally)
    {

        // With a Box we can put Text and Canvas to the
        // same area on the screen.

        Box(
            modifier = Modifier
                .requiredWidth(screenWidth)
                .weight(8F)
        )
        {
            Text(
                "Ball offset: ( ${ballOffsetX.floatValue}" +
                        ", ${ballOffsetY.floatValue} )",
                fontSize = 20.sp
            )

            // As the area reserved for Canvas is now determined
            // by the Box, we can just fill all that area.

            Canvas(
                modifier = Modifier.fillMaxSize()
            )
            {
                val canvasWidth = size.width
                val canvasHeight = size.height

                drawCircle(
                    color = ballColor.value,
                    center = Offset(
                        x = canvasWidth / 2 + ballOffsetX.floatValue,
                        y = canvasHeight / 2 + ballOffsetY.floatValue
                    ),
                    radius = size.minDimension / 4
                )

                drawCircle(
                    color = Color.Black,
                    center = Offset(
                        x = canvasWidth / 2 + ballOffsetX.floatValue,
                        y = canvasHeight / 2 + ballOffsetY.floatValue
                    ),
                    radius = size.minDimension / 4,
                    style = Stroke(width = 4F)
                )
            }
        }

        // DropDownMenu is inside a Box

        Box(contentAlignment = Alignment.Center)
        {
            DropdownMenu(
                expanded = menuShowOnOff.value,
                onDismissRequest = { menuShowOnOff.value = false }
            ) {
                DropdownMenuItem(onClick = { }, text = { Text("Choose Ball Color") })

                HorizontalDivider(thickness = 2.dp)
                DropdownMenuItem(onClick =
                {
                    ballColor.value = Color.Red
                    menuShowOnOff.value = false

                }, text = { Text("RED") })

                DropdownMenuItem(onClick =
                {
                    ballColor.value = Color.White
                    menuShowOnOff.value = false
                }, text = { Text("WHITE") })

                DropdownMenuItem(onClick =
                {
                    ballColor.value = Color.Green
                    menuShowOnOff.value = false
                }, text = { Text("GREEN") })

                DropdownMenuItem(onClick =
                {
                    ballColor.value = Color.Yellow
                    menuShowOnOff.value = false
                }, text = { Text("YELLOW") })

                DropdownMenuItem(onClick =
                {
                    ballColor.value = Color.Blue
                    menuShowOnOff.value = false
                }, text = { Text("BLUE") })

                DropdownMenuItem(onClick = {
                    ballColor.value = Color.Cyan
                    menuShowOnOff.value = false
                }, text = { Text("CYAN") })

            }
        }

        // Next we'll specify the button rows.
        // Button heights relate to screen height.
        // This way the buttons look good also on some
        // smaller screens.

        HorizontalDivider(thickness = 3.dp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(2F)
        )
        {

            BallControlButton(
                onClick =  // Reset button
                {
                    ballOffsetX.floatValue = 0f
                    ballOffsetY.floatValue = 0f
                    ballColor.value = Color.Red
                },
                modifier = Modifier
                    .requiredHeight(screenHeight / 8)
                    .requiredWidth(100.dp)
                    .weight(1F),
                "X"
            )

            BallControlButton(
                onClick =  // Up button
                {
                    ballOffsetY.value -= 8
                },
                modifier = Modifier
                    .requiredHeight(screenHeight / 8)
                    .requiredWidth(100.dp)
                    .weight(1F),
                "^"
            )


            BallControlButton(
                onClick =  // Color button
                {
                    menuShowOnOff.value = true
                },
                modifier = Modifier
                    .requiredHeight(screenHeight / 8)
                    .requiredWidth(100.dp)
                    .weight(1F),
                "\uD83C\uDFA8"
            )
        } // End row

        // Bottom row begins

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(2F)
        )
        {

            BallControlButton(
                onClick =  // Left button
                {
                    ballOffsetX.value -= 8
                },
                modifier = Modifier
                    .requiredHeight(screenHeight / 8)
                    .requiredWidth(100.dp)
                    .weight(1F),
                "<"
            )

            BallControlButton(
                onClick =  // Down button
                {
                    ballOffsetY.floatValue += 8
                },
                modifier = Modifier
                    .requiredHeight(screenHeight / 8)
                    .requiredWidth(100.dp)
                    .weight(1F),
                "||"
            )

            BallControlButton(
                onClick =  // Right button
                {
                    ballOffsetX.floatValue += 8
                },
                modifier = Modifier
                    .requiredHeight(screenHeight / 8)
                    .requiredWidth(100.dp)
                    .weight(1F),
                ">"
            )

        } // End row
    }
}


