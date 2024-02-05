package com.vsloong.toolman.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.toolman.ui.themes.R

@Composable
fun AppTextFiled(
    text: String,
    hint: String = "",
    modifier: Modifier = Modifier,
    background: Color = Color(0xFFF3F1F1),
    onValueChange: (String) -> Unit,
    textAlignCenter: Boolean = true
) {

    BasicTextField(
        value = text,
        textStyle = TextStyle.Default.copy(
            fontSize = 16.sp,
        ),
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color = background),
        onValueChange = onValueChange,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                contentAlignment = if (textAlignCenter) {
                    Alignment.CenterStart
                } else {
                    Alignment.TopStart
                }
            ) {
                if (text.isEmpty()) {
                    Text(text = hint)
                }

                innerTextField()
            }
        }
    )

}

@Composable
fun AppTextFiled1(
    hint: String = "",
    modifier: Modifier = Modifier,
    onSend: (String) -> Unit,
) {

    val text = remember {
        mutableStateOf("")
    }

    BasicTextField(
        value = text.value,
        textStyle = TextStyle.Default.copy(
            fontSize = 16.sp,
        ),
        modifier = modifier.onPreviewKeyEvent {
            if (it.isCtrlPressed && it.key == Key.Enter && it.type == KeyEventType.KeyDown) {
                onSend.invoke(text.value)
                text.value = ""
                true
            }

            false
        },
        onValueChange = {
            text.value = it
        },
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            Box(
                modifier = Modifier,
            ) {
                if (text.value.isEmpty()) {
                    Text(text = hint)
                }

                innerTextField()
            }
        }
    )

}