package com.vsloong.toolman

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.vsloong.toolman.ui.content.FeedsScreen
import com.vsloong.toolman.ui.device.DeviceContent
import com.vsloong.toolman.ui.tab.LeftTab
import com.vsloong.toolman.ui.themes.R


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
                    .clip(RoundedCornerShape(48.dp))
                    .background(color = R.colors.background),
            ) {
                WindowDraggableArea(
                    modifier = Modifier.fillMaxWidth().height(46.dp).background(color = Color.Blue)
                )

                Row(modifier = Modifier.fillMaxSize()) {

                    // 左侧栏
                    Column(modifier = Modifier.fillMaxSize().weight(1f)) {
                        LeftTab()
                    }

                    // 中间功能栏
                    Column(modifier = Modifier.fillMaxSize().weight(2f)) {
                        Navigator(FeedsScreen()){
                            CurrentScreen()
                        }
                    }

                    // 右侧栏
                    Column(modifier = Modifier.fillMaxSize().weight(1f)) {
                        DeviceContent()
                    }
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
    size: DpSize = DpSize(1060.dp, 750.dp),
): WindowState = rememberWindowState(
    placement = placement,
    isMinimized = isMinimized,
    position = position,
    size = size
)