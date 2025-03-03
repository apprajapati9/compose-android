package ca.apprajapati.composer.screens.gestures

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlin.math.roundToInt

@Composable
fun GenericDragDemo() {
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    var size by remember { mutableStateOf(Size.Zero) }
    Box(Modifier.fillMaxSize().onSizeChanged { size = it.toSize() }) {
        Box(
            Modifier.offset { IntOffset(offsetX.floatValue.roundToInt(), offsetY.floatValue.roundToInt()) }
                .size(50.dp)
                .background(Color.Blue)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount -> // dragAmount is an offset of how much amount is moved towards from the last point i.e 0,0

                        val original = Offset(offsetX.floatValue, offsetY.floatValue)
                        val summed = original + dragAmount
                        Log.d("Ajay", "Drag -> change -> $change, amount -> $dragAmount, original -> $original summed -> $summed")
                        val newValue =
                            Offset(
                                x = summed.x.coerceIn(0f, size.width - 50.dp.toPx()),
                                y = summed.y.coerceIn(0f, size.height - 50.dp.toPx())
                            )
                        offsetX.floatValue = newValue.x
                        offsetY.floatValue = newValue.y
                    }
                }
        )
    }
}