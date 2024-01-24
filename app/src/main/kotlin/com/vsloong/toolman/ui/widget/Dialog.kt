package com.vsloong.toolman.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition


@Composable
fun AppDialog(
    visible: Boolean,
    size: DpSize = DpSize(width = 600.dp, height = 400.dp),
    content: @Composable ColumnScope.() -> Unit
) {

    DialogWindow(
        onCloseRequest = {

        },
        visible = visible,
        state = DialogState(
            position = WindowPosition.Aligned(alignment = Alignment.Center),
            size = size
        ),
        undecorated = true,
        transparent = true,
    ) {

        // Card来实现阴影效果
        Card(
            elevation = 8.dp,
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White)
            ) {
                content()
            }
        }
    }

}