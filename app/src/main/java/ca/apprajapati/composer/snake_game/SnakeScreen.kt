package ca.apprajapati.composer.snake_game

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
    scaleFactor: attribute to define how big a cell would be.
 */

@Composable
fun SnakeScreen(viewModel: SnakeViewModel, scaleFactor: Float) {

    val textMeasurer = rememberTextMeasurer()

    val snake = viewModel.snake.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {

        Canvas(modifier = Modifier
            .padding(5.dp)
            .fillMaxSize()) {

            val height = size.height
            val width = size.width

            drawText(
                textMeasurer = textMeasurer,
                text = "Snake board W/H = $width, $height",
                topLeft = Offset(x = 100f, y = 100f),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            drawRect(
                color = Color.Black,
                topLeft = Offset(0f, 0f),
                style = Stroke(1.dp.toPx())
            )


            val scaleX = 40f // 1440/20 = 72 lines.
            val scaleY = 40f // 2585/60 = 43 lines.

            scale(
                scaleX = scaleX,
                scaleY = scaleY,
                pivot = Offset(0f, 0f)
            ) { // 1404/ 10 = 140, 2585/10 = 258

                val rows = (height / scaleY).toInt() //y++ to
                val columns = (width / scaleX).toInt()// x++ to bottom y

                viewModel.storeBoard(columns - 1, rows - 1)

                Log.d("Ajay", "Lines rows/columns = $rows, $columns")

                for (i in 0..columns) { //70
                    drawLine(
                        color = Color.Blue,
                        start = Offset(i.toFloat(), 0f),
                        end = Offset(i.toFloat(), rows.toFloat()),
                    ) // vertical y++
                }

                for (i in 0..rows) {
                    drawLine(
                        color = Color.Blue,
                        start = Offset(0f, i.toFloat()),
                        end = Offset(columns.toFloat(), i.toFloat()),
                        strokeWidth = 1 / scaleY,
                    ) // horizontal x++
                }

                drawRect(color = Color.Red, topLeft = Offset(0f, 0f), size = Size(1f, 1f))
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(69f, 22f),
                    size = Size(1f, 1f)
                )


                when (snake.value) {
                    is SnakeState.Alive -> {
                        val list = (snake.value as SnakeState.Alive).snake

                        for (i in list) {
                            Log.d("Ajay", "snake list -> $i")
                            drawRect(
                                color = Color.Black, topLeft = Offset(i.x, i.y),
                                size = Size(1f, 1f)
                            )
                        }

                    }

                    SnakeState.Init -> {
                        Log.d("Ajay", "SnakeScreen :: snake is init.")
                    }

                    is SnakeState.Dead -> {
                        Log.d("Ajay", "SnakeScreen :: snake is dead.")
                    }
                }
            }

            //drawGridColors(rows, columns, canvas = this)
        }

    }

}

/*
    Call this function to draw grid colors
 */
fun drawGridColors(rows: Int, columns: Int, canvas: DrawScope) {
    //columns x++, rows y++
    for (i in 0..<rows) { //y++
        for (j in 0..<columns) { // x++
            val v = i + j
            if (v % 2 == 0) {
                canvas.drawRect(
                    color = Color.Black,
                    topLeft = Offset(j.toFloat(), i.toFloat()),
                    size = Size(1f, 1f)
                )

            } else {
                canvas.drawRect(
                    color = Color.White,
                    topLeft = Offset(j.toFloat(), i.toFloat()),
                    size = Size(1f, 1f)
                )
            }
        }
    }
}