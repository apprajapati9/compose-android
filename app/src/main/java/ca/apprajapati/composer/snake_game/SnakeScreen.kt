package ca.apprajapati.composer.snake_game

import android.graphics.RectF
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.abs
import kotlin.random.Random

/*
    scaleFactor: attribute to define how big a cell would be.
 */

@Composable
fun SnakeScreen(viewModel: SnakeViewModel, scaleFactor: Float) {

    val textMeasurer = rememberTextMeasurer()

    val snake = viewModel.snake.collectAsStateWithLifecycle()

    val startSwipe = remember {
        mutableStateOf(Offset(0f, 0f))
    }

    val endSwipe = remember {
        mutableStateOf(Offset(0f, 0f))
    }

    val isUpdateFood = remember {
        mutableStateOf(true)
    }

    val foodLocation = remember {
        mutableStateOf(Offset(0f, 0f))
    }

    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing)
        )
    )

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectHorizontalDragGestures(onHorizontalDrag = { change, _ ->
                    endSwipe.value = change.position
                },
                    onDragStart = { offset ->
                        startSwipe.value = offset
                        Log.d("Ajay", "Start $offset")

                    },
                    onDragEnd = {

                        val x = abs(endSwipe.value.x - startSwipe.value.x)
                        val y = abs(endSwipe.value.y - startSwipe.value.y)

                        if (x > y) {
                            if (endSwipe.value.x > startSwipe.value.x) {
                                viewModel.updateDirection(Direction.RIGHT)
                                Log.d("Ajay", "Right swipe")
                            }
                            if (endSwipe.value.x < startSwipe.value.x) {
                                viewModel.updateDirection(Direction.LEFT)
                                Log.d("Ajay", "Left swipe")

                            }
                        } else {
                            if (endSwipe.value.y > startSwipe.value.y) {
                                viewModel.updateDirection(Direction.DOWN)
                                Log.d("Ajay", "Down swipe")

                            }
                            if (endSwipe.value.y < startSwipe.value.y) {
                                viewModel.updateDirection(Direction.UP)
                                Log.d("Ajay", "Up swipe")
                            }
                        }
                    }
                )
            }
            .fillMaxSize()
            .background(Color.Transparent)
    ) {

        Canvas(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
        ) {

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

//            drawRect(
//                color = Color.Black,
//                topLeft = Offset(0f, 0f),
//                style = Stroke(1.dp.toPx())
//            )


            val scaleX = 92f // 1440/20 = 72 lines.
            val scaleY = 92f // 2585/60 = 43 lines.

            scale(
                scaleX = scaleX,
                scaleY = scaleY,
                pivot = Offset(0f, 0f)
            ) { // 1404/ 10 = 140, 2585/10 = 258

                val rows = (height / scaleY).toInt() //y++ to
                val columns = (width / scaleX).toInt()// x++ to bottom y


                if (isUpdateFood.value) {
                    val rX = Random.nextInt(1, columns - 1).toFloat()
                    val rY = Random.nextInt(1, rows - 1).toFloat()
                    foodLocation.value = Offset(rX, rY)
                    isUpdateFood.value = false
                }

                viewModel.storeBoard(columns - 1, rows - 1)

                //Log.d("Ajay", "Lines rows/columns = $rows, $columns")

//                for (i in 0..columns) { //70
//                    drawLine(
//                        color = Color.Blue,
//                        start = Offset(i.toFloat(), 0f),
//                        end = Offset(i.toFloat(), rows.toFloat()),
//                    ) // vertical y++
//                }
//
//                for (i in 0..rows) {
//                    drawLine(
//                        color = Color.Blue,
//                        start = Offset(0f, i.toFloat()),
//                        end = Offset(columns.toFloat(), i.toFloat()),
//                        strokeWidth = 1 / scaleY,
//                    ) // horizontal x++
//                }

                drawGridColors(rows, columns, canvas = this)

                drawRect(color = Color.Red, topLeft = Offset(0f, 0f), size = Size(1f, 1f))
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(columns.toFloat() - 1, rows.toFloat() - 1),
                    size = Size(1f, 1f)
                )

                //Random food point.
                rotate(
                    degrees = angle,
                    pivot = Offset(
                        foodLocation.value.x + 0.5f,
                        foodLocation.value.y + 0.5f
                    ) //0.5 is halfway of a 1 single cell to center the rotation point.
                ) {
                    drawRect(
                        color = Color.Blue.copy(alpha = lerp(0f, 255f, angle)),
                        topLeft = Offset(foodLocation.value.x, foodLocation.value.y),
                        size = Size(1f, 1f)
                    )
                }

                drawCircle(color = Color.Red, center = Offset(12f + 0.5f , 12f + 0.5f), radius = 0.5f)

                //Log.d("Ajay", "Food:: ${foodLocation.value}")


                when (snake.value) {
                    is SnakeState.Alive -> {
                        val list = (snake.value as SnakeState.Alive).snake

                        for (i in list.indices) {
                            //Log.d("Ajay", "snake list -> $i")
                            if (i % 2 == 0) {

                                drawCircle(color = Color.Black.copy(alpha = 0.7f),
                                    radius = 0.5f,
                                    center = Offset(list[i].x + 0.5f, list[i].y + 0.5f))

//                                drawRect(
//                                    color = Color.Black.copy(alpha = 0.7f),
//                                    topLeft = Offset(list[i].x, list[i].y),
//                                    size = Size(1f, 1f)
//                                )

                            } else {

                                drawCircle(color = Color.Green,
                                    radius = 0.5f,
                                    center = Offset(list[i].x + 0.5f, list[i].y + 0.5f))
//                                drawRect(
//                                    color = Color.Green,
//                                    topLeft = Offset(list[i].x, list[i].y),
//                                    size = Size(1f, 1f)
//                                )
                            }

                            if (i == 0 && list[i].x == foodLocation.value.x && list[i].y == foodLocation.value.y) {
                                isUpdateFood.value = true
                                viewModel.extendSnake(true)
                            } else {
                                viewModel.extendSnake(false)
                            }
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
                    color = Color(0x00000010),
                    topLeft = Offset(j.toFloat(), i.toFloat()),
                    size = Size(1f, 1f)
                )

            } else {
                canvas.drawRect(
                    color = Color.Green .copy(alpha = 0.1f),
                    topLeft = Offset(j.toFloat(), i.toFloat()),
                    size = Size(1f, 1f)
                )
            }
        }
    }
}