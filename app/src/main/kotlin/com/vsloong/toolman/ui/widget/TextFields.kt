package com.vsloong.toolman.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.toolman.ui.themes.R

@Composable
fun AppTextFiled(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {

    BasicTextField(
        value = text,
        textStyle = TextStyle.Default.copy(
            fontSize = 16.sp,
        ),
        modifier = modifier.clip(RoundedCornerShape(50))
            .background(R.colors.background),
        onValueChange = onValueChange,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (text.isEmpty()) {
                    Text(text = "请输入")
                }

                innerTextField()
            }
        }
    )

}