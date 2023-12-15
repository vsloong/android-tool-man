package com.vsloong.toolman.ui.widget.ext

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vsloong.toolman.ui.widget.utils.dp2px
import kotlin.random.Random

private fun randomComposeColor(): Color {
    return Color(
        alpha = 255,
        red = Random.nextInt(256),
        green = Random.nextInt(256),
        blue = Random.nextInt(256)
    )
}

// 扩展的随机背景色修饰符，每次重组都会显示不同颜色
fun Modifier.randomBackground() = this
    .background(
        color = randomComposeColor(),
        shape = RoundedCornerShape(4.dp)
    )
    .padding(4.dp)


fun Modifier.dashBorder(
    color: Color,
    strokeWidth: Dp = 1.dp,
    cornerRadiusDp: Dp = 8.dp
) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }
        val cornerRadiusPx = density.run { cornerRadiusDp.toPx() }

        this.then(
            Modifier.drawWithCache {
                onDrawBehind {
                    val stroke = Stroke(
                        width = strokeWidthPx,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(
                                cornerRadiusPx,
                                cornerRadiusPx
                            ), 0f
                        )
                    )

                    drawRoundRect(
                        color = color,
                        style = stroke,
                        cornerRadius = CornerRadius(cornerRadiusPx)
                    )
                }
            }
        )
    }
)
