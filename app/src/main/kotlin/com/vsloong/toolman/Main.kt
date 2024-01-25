package com.vsloong.toolman

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.vsloong.toolman.core.common.model.AdbDeviceInfo
import com.vsloong.toolman.manager.DeviceManager
import com.vsloong.toolman.ui.screen.feeds.FeedsScreen
import com.vsloong.toolman.ui.home.HomeViewModel
import com.vsloong.toolman.ui.screen.apps.AppsScreen
import com.vsloong.toolman.ui.screen.channel.ChannelScreen
import com.vsloong.toolman.ui.screen.feature.FeatureScreen
import com.vsloong.toolman.ui.screen.sign.SignScreen
import com.vsloong.toolman.ui.tab.LeftTabEvent
import com.vsloong.toolman.ui.tab.TabType
import com.vsloong.toolman.ui.themes.R
import com.vsloong.toolman.ui.widget.DragAndDropBox
import java.nio.file.Path

@Composable
fun appWindowState(
    placement: WindowPlacement = WindowPlacement.Floating,
    isMinimized: Boolean = false,
    position: WindowPosition = WindowPosition.Aligned(Alignment.Center),
    size: DpSize = DpSize(1060.dp, 680.dp),
): WindowState = rememberWindowState(
    placement = placement,
    isMinimized = isMinimized,
    position = position,
    size = size
)

val homeViewModel = HomeViewModel()


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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = R.colors.background),
            ) {
                WindowDraggableArea(
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                )

                Navigator(FeedsScreen()) { navigator ->

                    Row(modifier = Modifier.fillMaxSize()) {

                        // 左侧栏
                        Column(modifier = Modifier.fillMaxSize().weight(1f)) {
                            LeftTab(
                                currentTab = homeViewModel.currentTab.value,
                                leftTabEvent = LeftTabEvent(
                                    onHomeClick = {
                                        homeViewModel.tabEvent.onHomeClick.invoke()
                                        navigator.replace(FeedsScreen())
                                    },
                                    onFeatureClick = {
                                        homeViewModel.tabEvent.onFeatureClick.invoke()
                                        navigator.replace(FeatureScreen())
                                    },
                                    onAppsClick = {
                                        homeViewModel.tabEvent.onAppsClick.invoke()
                                        navigator.replace(AppsScreen())
                                    },
                                    onChannelClick = {
                                        homeViewModel.tabEvent.onChannelClick.invoke()
                                        navigator.replace(ChannelScreen())
                                    },
                                    onSignClick = {
                                        homeViewModel.tabEvent.onSignClick.invoke()
                                        navigator.replace(SignScreen())
                                    }
                                )
                            )
                        }

                        // 中间功能栏
                        Column(modifier = Modifier.fillMaxSize().weight(3f)) {
                            CurrentScreen()
                        }

                        // 右侧栏
                        Column(modifier = Modifier.fillMaxSize().weight(1f)) {
                            DeviceContent(
                                devices = DeviceManager.devices,
                                onFileDrop = { files, device ->
                                    homeViewModel.install(files, setOf(device))
                                },

                                onRefreshClick = {
                                    homeViewModel.refreshDevices()
                                })
                        }
                    }
                }


            }
        }

    }
}


@Composable
fun DeviceContent(
    devices: List<AdbDeviceInfo>,
    onFileDrop: (List<Path>, AdbDeviceInfo) -> Unit,
    onRefreshClick: () -> Unit,
) {

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = "设备", fontSize = 24.sp, modifier = Modifier.wrapContentSize()
            .clickable {
                onRefreshClick.invoke()
            })

        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(devices) {
                DeviceItem(
                    device = it,
                    onFileDrop = onFileDrop
                )
            }
        }
    }
}

@Composable
private fun DeviceItem(
    device: AdbDeviceInfo,
    onFileDrop: (List<Path>, AdbDeviceInfo) -> Unit
) {
    DragAndDropBox(
        modifier = Modifier.fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFC498FF)),
        onDrop = { paths ->
            onFileDrop.invoke(paths, device)
        },
        contentAlignment = Alignment.BottomCenter
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
                .height(32.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(50))
                .background(color = Color.White)
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = device.model,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun LeftTab(
    currentTab: TabType,
    leftTabEvent: LeftTabEvent
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "ATM",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )

        Text(text = "your android tool man")

        Spacer(modifier = Modifier.size(36.dp))

        TabItem(
            resourcePath = "tab_left_custom.svg",
            tabName = "Message Feed",
            isSelected = currentTab == TabType.Feed,
            onClick = leftTabEvent.onHomeClick
        )

        TabItem(
            resourcePath = "tab_left_custom.svg",
            tabName = "Feature",
            isSelected = currentTab == TabType.Feature,
            onClick = leftTabEvent.onFeatureClick
        )

        TabItem(
            resourcePath = "tab_left_custom.svg",
            tabName = "Apps",
            isSelected = currentTab == TabType.Apps,
            onClick = leftTabEvent.onAppsClick
        )

        TabItem(
            resourcePath = "tab_left_custom.svg",
            tabName = "Channel",
            isSelected = currentTab == TabType.Channel,
            onClick = leftTabEvent.onChannelClick
        )

        TabItem(
            resourcePath = "tab_left_custom.svg",
            tabName = "Sign",
            isSelected = currentTab == TabType.Sign,
            onClick = leftTabEvent.onSignClick
        )

    }
}

@Composable
private fun TabItem(
    resourcePath: String,
    tabName: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = if (isSelected) {
                    Color.Black
                } else {
                    Color.Transparent
                }
            )
            .clickable {
                onClick.invoke()
            }
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Image(
            painter = painterResource(resourcePath),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(
                color = if (isSelected) {
                    Color.White
                } else {
                    Color.Black
                }
            )
        )

        Text(
            text = tabName,
            fontSize = 16.sp,
            color = if (isSelected) {
                Color.White
            } else {
                Color.Black
            }
        )

    }
}