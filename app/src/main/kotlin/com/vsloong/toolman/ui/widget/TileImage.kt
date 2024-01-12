package com.vsloong.toolman.ui.widget

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource


/**
 * 图片平铺
 */
@Composable
fun TileImage(
    modifier: Modifier,
    resourcePath: String
) {
    Canvas(
        modifier = modifier
            // 注意需要使用裁剪修饰符，否则绘制图片内容会超出区域
            .clip(RectangleShape)
    ) {

        val pattern = useResource(resourcePath, ::loadImageBitmap)

        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            shader = ImageShader(pattern, TileMode.Repeated, TileMode.Repeated)
        }

        drawIntoCanvas {
            it.nativeCanvas.drawPaint(paint)
        }
        paint.reset()
    }
}

@Composable
fun TileImage2(
    modifier: Modifier,
    resourcePath: String
) {
    val pattern = useResource(resourcePath, ::loadImageBitmap)

    Canvas(
        modifier = modifier
            // 注意需要使用裁剪修饰符，否则绘制图片内容会超出区域
            .clip(RectangleShape)
    ) {
        val totalWidth = size.width / pattern.width
        val totalHeight = size.height / pattern.height

        var x = 0f
        var y = 0f
        for (i in 0..totalHeight.toInt()) {
            y = (i * pattern.height).toFloat()
            for (j in 0..totalWidth.toInt()) {
                x = (j * pattern.width).toFloat()

                drawImage(
                    pattern,
                    colorFilter = null,
                    topLeft = Offset(x, y)
                )
            }
        }
    }
}