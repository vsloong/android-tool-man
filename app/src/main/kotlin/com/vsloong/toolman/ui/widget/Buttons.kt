package com.vsloong.toolman.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vsloong.toolman.ui.themes.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = R.colors.primary,
    clickable: Boolean = true,
) {
    Surface(
        onClick = onClick,
        enabled = clickable,
        modifier = modifier
            .widthIn(min = 100.dp)
            .heightIn(min = 40.dp),
        color = if (clickable) {
            backgroundColor
        } else {
            backgroundColor.copy(alpha = 0.25f)
        },
        contentColor = Color.Transparent,
        shape = RoundedCornerShape(50)
    ) {

        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = R.colors.background
            )
        }
    }
}