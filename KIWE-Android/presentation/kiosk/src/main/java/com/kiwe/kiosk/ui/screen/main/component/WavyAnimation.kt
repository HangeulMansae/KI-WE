package com.kiwe.kiosk.ui.screen.main.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.sin

@Composable
fun WavyAnimation(
    modifier: Modifier = Modifier,
    waveColor: Color = Color(0xFFC49202), // 색깔 바꿔야됨
    wavelength: Float = 200f,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec =
            infiniteRepeatable(
                animation = tween(5000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "",
    )

    Canvas(modifier = modifier) {
        drawWave(
            waveOffset = waveOffset,
            amplitude = 180f,
            wavelength = wavelength,
            waveColor = waveColor.copy(alpha = 0.2f),
        )
    }
}

private fun DrawScope.drawWave(
    waveOffset: Float,
    amplitude: Float,
    wavelength: Float,
    waveColor: Color,
) {
    val path =
        Path().apply {
            moveTo(0f, size.height / 2)
            for (x in 0 until size.width.toInt()) {
                val y =
                    size.height / 2 + amplitude * sin((x / wavelength + waveOffset).toDouble()).toFloat()
                lineTo(x.toFloat(), y)
            }
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

    drawPath(path, color = waveColor)
}
