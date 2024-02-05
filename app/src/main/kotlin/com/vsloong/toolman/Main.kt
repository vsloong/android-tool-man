package com.vsloong.toolman

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.vsloong.toolman.ui.home.HomeScreen

@Composable
fun appWindowState(
    placement: WindowPlacement = WindowPlacement.Floating,
    isMinimized: Boolean = false,
    position: WindowPosition = WindowPosition.Aligned(Alignment.Center),
    size: DpSize = DpSize(1280.dp, 800.dp),
): WindowState = rememberWindowState(
    placement = placement,
    isMinimized = isMinimized,
    position = position,
    size = size
)


/**
 * 请参考
 * https://github.com/JetBrains/compose-multiplatform
 *
 * Tutorials
 * https://github.com/JetBrains/compose-multiplatform/blob/master/tutorials/README.md
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = appWindowState(),
        transparent = true,
        undecorated = true
    ) {
        MaterialTheme {
            HomeScreen(
                onExit = {
                    exitApplication()
                }
            )
        }
    }
}