package com.vsloong.toolman.ui.widget

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

@Composable
fun AppProgressBar(
    progress: Float,
    modifier: Modifier,
    color: Color,
    backgroundColor: Color,
) {
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier.clip(CircleShape),
        color = color,
        backgroundColor = backgroundColor
    )
}

@Composable
fun AppProgressBar(
    modifier: Modifier,
    color: Color,
    backgroundColor: Color,
) {
    LinearProgressIndicator(
        modifier = modifier.clip(CircleShape),
        color = color,
        backgroundColor = backgroundColor
    )
}