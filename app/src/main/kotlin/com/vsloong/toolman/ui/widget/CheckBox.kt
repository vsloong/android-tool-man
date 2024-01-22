package com.vsloong.toolman.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AppCheckBox(
    checked: Boolean,
    text: String,
    onCheckedClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Image(
            painter = painterResource(
                if (checked) {
                    "ic_checkbox_selected.svg"
                } else {
                    "ic_checkbox_unselect.svg"
                }
            ),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
                .clip(CircleShape)
                .clickable {
                    onCheckedClick.invoke()
                }
        )

        Text(text = text, fontSize = 16.sp, color = Color.Black)
    }
}