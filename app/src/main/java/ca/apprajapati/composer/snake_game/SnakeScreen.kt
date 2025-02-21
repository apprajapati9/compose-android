package ca.apprajapati.composer.snake_game

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

/*
    scaleFactor: attribute to define how big a cell would be.
 */

@Composable
fun SnakeScreen(viewModel: SnakeViewModel, scaleFactor: Float) {

    val textMeasurer = rememberTextMeasurer()

    val snake by viewModel.snake.collectAsStateWithLifecycle()

    val startSwipe = remember {
        mutableStateOf(Offset(0f, 0f))
    }

    val score = remember {
        mutableIntStateOf(0)
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
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = tween(800, easing = LinearEasing)
        )
    )

    val d = LocalDensity.current.density
    val h = LocalConfiguration.current.screenHeightDp * d//LocalDensity.current.density.dp
    val w = LocalConfiguration.current.screenWidthDp * d //LocalDensity.current.density.dp


    LaunchedEffect(Unit) {
        Log.d("Ajay", "LaunchedEffect: Alive Snake $w,$h -- ${w / scaleFactor}")
        viewModel.restartSnake(w / scaleFactor)
    }

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
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    Log.d("Ajay", "Double tap detected!")
                    viewModel.restartSnake(w / scaleFactor)
                })

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
                text = "score = ${score.intValue} \n Double tap to restart!",
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


            val scaleX = scaleFactor // 1440/20 = 72 lines.
            val scaleY = scaleFactor // 2585/60 = 43 lines.

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


                //drawCircle(color = Color.Red, center = Offset(12f + 0.5f , 12f + 0.5f), radius = 0.5f)

                //Log.d("Ajay", "Food:: ${foodLocation.value}")


                when (val state = snake) {
                    is SnakeState.Alive -> {
                        val list = state.snake

                        for (i in list.indices) {
                            //Log.d("Ajay", "snake list -> $i")
                            if (i % 2 == 0) {

                                drawCircle(
                                    color = Color.Black.copy(alpha = 0.7f),
                                    radius = 0.5f,
                                    center = Offset(list[i].x + 0.5f, list[i].y + 0.5f)
                                )

                            } else {

                                drawCircle(
                                    color = Color.Green,
                                    radius = 0.5f,
                                    center = Offset(list[i].x + 0.5f, list[i].y + 0.5f)
                                )
                            }
                            if (i == 0) {

                                if (list[i].x == foodLocation.value.x && list[i].y == foodLocation.value.y) {
                                    score.intValue += 1
                                    isUpdateFood.value = true
                                    viewModel.extendSnake(true)

                                }

                                val counter = list.count { offset ->
                                    offset == Offset(list[i].x, list[i].y)
                                }

                                if (counter > 1) {
                                    viewModel.updateSnakeState(
                                        state = SnakeState.Dead(list)
                                    )
                                }
                            } else {
                                viewModel.extendSnake(false)
                            }

                        }

                    }

                    SnakeState.Init -> {
                        Log.d("Ajay", "SnakeScreen :: snake is init.")
                    }

                    is SnakeState.Dead -> {
                        Log.d("Ajay", "SnakeScreen :: snake is dead. $alpha")
                        score.intValue = 0
                        val list = state.snake
                        for (i in list.indices) {
                            if (i % 2 == 0) {
                                drawCircle(
                                    color = Color.Black.copy(alpha = alpha),
                                    radius = 0.5f,
                                    center = Offset(list[i].x + 0.5f, list[i].y + 0.5f)
                                )

                            } else {
                                drawCircle(
                                    color = Color.Green.copy(alpha = alpha),
                                    radius = 0.5f,
                                    center = Offset(list[i].x + 0.5f, list[i].y + 0.5f)
                                )
                            }
                        }
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
                    color = Color.Green.copy(alpha = 0.1f),
                    topLeft = Offset(j.toFloat(), i.toFloat()),
                    size = Size(1f, 1f)
                )
            }
        }
    }
}