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
import androidx.compose.ui.geometry.CornerRadius
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
            animation = tween(2000, easing = LinearEasing)
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
                    score.intValue = 0
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
                text = "Score = ${score.intValue} \nDouble tap to restart!",
                topLeft = Offset(x = 100f, y = 100f),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )


            val scaleX = scaleFactor // 1440/20 = 72 lines.
            val scaleY = scaleFactor // 2585/60 = 43 lines.

            scale(
                scaleX = scaleX,
                scaleY = scaleY,
                pivot = Offset(0f, 0f)
            ) {

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

                drawGridColors(rows, columns, canvas = this)

                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(0f, 0f),
                    size = Size(1f, 1f),
                    cornerRadius = CornerRadius(0.3f, 0.3f)
                )
                drawRoundRect(
                    color = Color.Red,
                    topLeft = Offset(columns.toFloat() - 1, rows.toFloat() - 1),
                    size = Size(1f, 1f),
                    cornerRadius = CornerRadius(0.3f, 0.3f)
                )

                //Random food point.
                rotate(
                    degrees = angle,
                    pivot = Offset(
                        foodLocation.value.x + 0.5f,
                        foodLocation.value.y + 0.5f
                    ) //0.5 is halfway of a 1 single cell to center the rotation point.
                ) {
                    drawRoundRect(
                        color = Color.Blue.copy(alpha = lerp(0f, 255f, angle)),
                        topLeft = Offset(foodLocation.value.x, foodLocation.value.y),
                        cornerRadius = CornerRadius(0.3f, 0.3f),
                        size = Size(1f, 1f)
                    )
                }

                when (val state = snake) {
                    is SnakeState.Alive -> {
                        val list = state.snake

                        for (i in list.indices) {

                            if (i % 2 == 0) {
                                drawRoundRect(
                                    color = Color.Cyan.copy(alpha = 0.7f),
                                    topLeft = Offset(list[i].x, list[i].y),
                                    cornerRadius = CornerRadius(0.3f, 0.3f),
                                    size = Size(1f, 1f)
                                )
                            } else {
                                drawRoundRect(
                                    color = Color.Black.copy(alpha = 0.7f),
                                    topLeft = Offset(list[i].x, list[i].y),
                                    cornerRadius = CornerRadius(0.3f, 0.3f),
                                    size = Size(1f, 1f)
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
                        val list = state.snake
                        for (i in list.indices) {
                            if (i % 2 == 0) {
                                drawRoundRect(
                                    color = Color.Black.copy(alpha = alpha),
                                    topLeft = Offset(list[i].x, list[i].y),
                                    cornerRadius = CornerRadius(0.3f, 0.3f),
                                    size = Size(1f, 1f)
                                )
                            } else {
                                drawRoundRect(
                                    color = Color.Cyan.copy(alpha = alpha),
                                    topLeft = Offset(list[i].x, list[i].y),
                                    size = Size(1f, 1f),
                                    cornerRadius = CornerRadius(0.3f, 0.3f)
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

//    for (i in 0..columns) { //70
//        canvas.drawLine(
//            color = Color.Blue,
//            start = Offset(i.toFloat(), 0f),
//            end = Offset(i.toFloat(), rows.toFloat()),
//        ) // vertical y++
//    }
//
//    for (i in 0..rows) {
//        canvas.drawLine(
//            color = Color.Blue,
//            start = Offset(0f, i.toFloat()),
//            end = Offset(columns.toFloat(), i.toFloat()),
//        ) // horizontal x++
//    }


    //columns x++, rows y++
    for (i in 0..<rows) { //y++
        for (j in 0..<columns) { // x++
            val v = i + j
            if (v % 2 == 0) {
                canvas.drawRoundRect(
                    color = Color.LightGray.copy(alpha = 0.15f), //Color(0x00000010),
                    topLeft = Offset(j.toFloat(), i.toFloat()),
                    size = Size(1f, 1f),
                    cornerRadius = CornerRadius(0.3f, 0.3f)
                )

            } else {
                canvas.drawRoundRect(
                    color = Color.LightGray.copy(alpha = 0.1f),
                    topLeft = Offset(j.toFloat(), i.toFloat()),
                    size = Size(1f, 1f),
                    cornerRadius = CornerRadius(0.3f, 0.3f)
                )
            }
        }
    }
}