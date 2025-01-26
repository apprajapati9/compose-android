package ca.apprajapati.composer.screens.drawing_demo

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlin.random.Random

@Composable
fun DrawingDemo() {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    var keyToTriggerAnimation by remember {
        mutableIntStateOf(0)
    }
    val animationProgress = remember {
        Animatable(0f)
    }

    val textMeasurer = rememberTextMeasurer()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {

        val lifecycle = lifecycleOwner.lifecycle

        val lifecycleEvents = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    Log.d("Ajay", "ON_CREATE")
                }

                Lifecycle.Event.ON_START -> {
                    Log.d("Ajay", "ON_START")
                }

                Lifecycle.Event.ON_RESUME -> {
                    keyToTriggerAnimation += 1
                    Log.d("Ajay", "ON_RESUME $keyToTriggerAnimation")
                }

                Lifecycle.Event.ON_PAUSE -> {
                    Log.d("Ajay", "ON_PAUSE")
                }

                Lifecycle.Event.ON_STOP -> {
                    Log.d("Ajay", "ON_STOP")
                }

                Lifecycle.Event.ON_DESTROY -> {
                    Log.d("Ajay", "ON_DESTROY")
                }

                Lifecycle.Event.ON_ANY -> {
                    Log.d("Ajay", "ON_ANY")
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleEvents)

        onDispose {
            Log.d("Ajay", "onDispose() triggered")
            lifecycle.removeObserver(lifecycleEvents)
        }

    }


    //TODO: animation is not triggering on Resume. Have to look more into it on how to achieve.
    LaunchedEffect(key1 = "Ajay") {
        Log.d("Ajay", "LaunchedEffect is triggered KEY= $keyToTriggerAnimation")
        animationProgress.animateTo(1f, tween(3000))
    }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .drawWithCache {

            val path = generateRandomData()

            val filledPath = Path()
            filledPath.addPath(path)
            filledPath.lineTo(0f, 999f)
            filledPath.lineTo(0f, 0f)
            filledPath.close()

            val brush = Brush.verticalGradient(
                listOf(
                    Color.Green.copy(alpha = 0.6f),
                    Color.Transparent
                )
            )

            onDrawBehind {
                val height = size.height
                val width = size.width

                translate(left = 50f, top = height / 2 + 100) {

                    val rectH = 1000f
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset.Zero,
                        size = Size(width - 100f, rectH), // w = 1340
                        style = Stroke(1.dp.toPx())
                    )

                    //Uncomment below code to show lines of graph.
//                    val gapSize = 100f
//                    val horizontalSize = 134f
//                    val lineSize = width - 100


//                    for (i in 1..10) {
//                        drawLine(
//                            color = Color.Black,
//                            start = Offset(x = 0f, y = gapSize * i),
//                            end = Offset(x = lineSize, y = gapSize * i),
//                            strokeWidth = 1.dp.toPx()
//                        )
//
//                        drawLine(
//                            color = Color.Black,
//                            start = Offset(x = horizontalSize * i, y = 0f),
//                            end = Offset(x = horizontalSize * i, y = rectH),
//                            strokeWidth = 1.dp.toPx()
//                        )
//                    }


                    clipRect(right = size.width * animationProgress.value) {
                        drawPath(path = path, color = Color.Blue, style = Stroke(2.dp.toPx()))
                        drawPath(path = filledPath, brush = brush, style = Fill)
                    }
                }
            }
        }) {

        val height = size.height
        val width = size.width

        drawText(
            textMeasurer = textMeasurer,
            text = "Ajay canvas W=$width, H=$height",
            topLeft = Offset(x = 20f, y = 40f),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )

        drawLine(
            color = Color.Green,
            start = Offset(x = screenWidth / 6f, y = screenHeight / 4f),
            end = Offset(x = (screenWidth / 6f) * 15f, y = screenHeight / 4f),
            strokeWidth = 8f
        )

        val rectSize = 300f


        drawRect(
            color = Color.Cyan,
            topLeft =
            Offset(
                x = width / 2f - rectSize / 2f, y = height / 2f - rectSize / 2f - height / 3f
            ),
            size = Size(rectSize, rectSize)
        )


        val outerRect = 320f
        drawRect(
            color = Color.Blue,
            topLeft =
            Offset(
                x = width / 2f - outerRect / 2f, y = height / 2f - outerRect / 2f - height / 3f
            ),
            size = Size(outerRect, outerRect),
            style = Stroke(width = 4f)
        )

        /*
drawArc angles visual
            270

     180           0

            90



Ctrl+ Q = for hovered information on function
 */
        translate(left = width / 2 - 250, top = height / 2 - 600f) {
            drawArc(
                color = Color.LightGray,
                startAngle = 45f, // 0 is 3 o'clock.
                sweepAngle = 270f, //size of the arc in degrees, clockwise, relative to startAngle
                useCenter = true,
                size = Size(500f, 500f),
            )
            /*
                In above example, starting Angle is 45 degrees and sweeping angle is 270
                which means adds 270 from the 45 degrees, 45+270 = 315.

                so pie would be 360-315 = 45,
                and 45 angle was from starting,
                so 45+45 = 90 degrees for the pie from 315 which will be the measurement for pie arch below.
             */

            drawArc(
                color = Color.Black,
                startAngle = 45f,
                sweepAngle = 270f,
                useCenter = true,
                size = Size(500f, 500f),
                style = Stroke(5f)
            )

            translate(left = 50f, top = 0f) {
                drawArc(
                    color = Color.Yellow,
                    startAngle = 315f,
                    sweepAngle = 90f,
                    useCenter = true,
                    size = Size(500f, 500f),
                )
                drawArc(
                    color = Color.Blue,
                    startAngle = 315f,
                    sweepAngle = 90f,
                    useCenter = true,
                    size = Size(500f, 500f),
                    style = Stroke(5f)
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1440px,height=2621px,dpi=560")
@Composable
fun DrawingDemoPreview() {
    DrawingDemo()
}


fun generateRandomData(): Path {
    val path = Path()
    val gap = 50

    path.moveTo(x = 0f, y = 0f)
    for (i in 0..19) {
        val randomX = Random.nextInt(i * gap, i * gap + gap) // 1 100, 2, 200
        val randomY = Random.nextInt(i * gap, i * gap + gap)

        path.lineTo(randomX.toFloat(), randomY.toFloat())
    }
    path.lineTo(1339f, 999f)

    return path
}