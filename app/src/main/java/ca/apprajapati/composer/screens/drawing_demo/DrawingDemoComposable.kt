package ca.apprajapati.composer.screens.drawing_demo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun DrawingDemo() {
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = Modifier
        .fillMaxSize()
        .drawWithCache {

            val data = generateRandomData()
            val path = Path()
            path.fillType = PathFillType.NonZero

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

                    val gapSize = 100f
                    val horizontalSize = 134f
                    val lineSize = width - 100

                    for (i in 1..10) {
                        drawLine(
                            color = Color.Black,
                            start = Offset(x = 0f, y = gapSize * i),
                            end = Offset(x = lineSize, y = gapSize * i),
                            strokeWidth = 1.dp.toPx()
                        )

                        drawLine(
                            color = Color.Black,
                            start = Offset(x = horizontalSize * i, y = 0f),
                            end = Offset(x = horizontalSize * i, y = rectH),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    withTransform({
                        //TODO: remove this, this is just experimental to flip the coordinates to showcase graph correctly.
                        translate(top = 1000f)
                        rotate(286f, pivot = Offset(0f, 0f))
                    }) {

                        path.moveTo(0f, 0f)
                        data.forEach {
                            path.lineTo(it.x, it.y)
                        }

//                    path.lineTo(10f,11f)
//                    path.lineTo(100f,110f)
//                    path.lineTo(150f,200f)
//                    path.lineTo(300f,270f)
//                    path.lineTo(350f,360f)
//                    path.lineTo(400f,450f)
//                    path.lineTo(600f,500f)
//                    path.lineTo(734f,500f)
//                    path.lineTo(600f,700f)
//                    path.lineTo(800f,788f)
//                    path.lineTo(1000f,965f)
//                    path.lineTo(1330f,1000f)
                        //path.close()

                        drawPath(path = path, color = Color.Blue, style = Stroke(2.dp.toPx()))
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


fun generateRandomData(): List<Offset> {
    val list = mutableListOf<Offset>()
    val gap = 100
    for (i in 1..10) {
        val randomX = Random.nextInt(i * gap, i * gap + gap) // 1 100, 2, 200
        val randomY = Random.nextInt(i * gap, i * gap + gap)
        list.add(Offset(randomX.toFloat(), randomY.toFloat()))
    }

    return list
}