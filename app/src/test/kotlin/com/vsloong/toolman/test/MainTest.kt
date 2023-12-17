package com.vsloong.toolman.test

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.vsloong.toolman.test.ui.screen.Screen1
import com.vsloong.toolman.test.ui.screen.Screen2

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = appWindowState(),
    ) {
        Column {

            Navigator(Screen1()) { navigator ->

                Column {
                    Text(text = "你好哦", fontSize = 32.sp)
                    Button(onClick = {
                        navigator.replace(Screen1())
                    }) {
                        Text(text = "场景1")
                    }

                    Button(onClick = {
                        navigator.replace(Screen2())
                    }) {
                        Text(text = "场景2")
                    }
                    CurrentScreen()
                }
            }
        }
    }
}

@Composable
fun appWindowState(
    placement: WindowPlacement = WindowPlacement.Floating,
    isMinimized: Boolean = false,
    position: WindowPosition = WindowPosition.Aligned(Alignment.Center),
    size: DpSize = DpSize(800.dp, 500.dp),
): WindowState = rememberWindowState(
    placement = placement,
    isMinimized = isMinimized,
    position = position,
    size = size
)