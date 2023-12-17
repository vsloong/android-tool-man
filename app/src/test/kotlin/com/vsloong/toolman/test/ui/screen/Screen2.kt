package com.vsloong.toolman.test.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen

class Screen2 : Screen {
    @Composable
    override fun Content() {

        val model = rememberScreenModel { Screen2Model() }

        Text(
            text = "Screen2",
            color = Color.Blue,
            fontSize = 32.sp,
            modifier = androidx.compose.ui.Modifier.clickable {
                model.log()
            }
        )
    }
}