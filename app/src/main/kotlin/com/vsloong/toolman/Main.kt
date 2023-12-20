package com.vsloong.toolman

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.core.common.usecase.BundleUseCase
import com.vsloong.toolman.manager.AssetsManager
import com.vsloong.toolman.ui.home.HomeScreen
import com.vsloong.toolman.ui.themes.R
import com.vsloong.toolman.ui.widget.AppButton
import com.vsloong.toolman.ui.widget.DragAndDropBox
import kotlin.io.path.Path


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = appWindowState(),
        transparent = true,
        undecorated = true
    ) {
        MaterialTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = R.colors.background)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                WindowDraggableArea(modifier = Modifier.fillMaxWidth().height(50.dp)) {

                }

                Navigator(HomeScreen()) {
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
    size: DpSize = DpSize(960.dp, 720.dp),
): WindowState = rememberWindowState(
    placement = placement,
    isMinimized = isMinimized,
    position = position,
    size = size
)