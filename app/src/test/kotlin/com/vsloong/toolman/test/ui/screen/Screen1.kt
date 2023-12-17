package com.vsloong.toolman.test.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class Screen1 : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        val model = rememberScreenModel { Screen1Model() }

        Column {

            Text(
                text = "跳转到场景2",
                color = Color.Blue,
                fontSize = 32.sp,
                modifier = Modifier.clickable {
                    navigator.replace(Screen2())
                }
            )

            Text(
                text = "打印日志",
                color = Color.Blue,
                fontSize = 32.sp,
                modifier = Modifier.clickable {
                    model.log()
                }
            )
        }
    }
}