package com.vsloong.toolman.ui.screen.apps

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.vsloong.toolman.base.BaseScreen
import com.vsloong.toolman.base.rememberViewModel
import com.vsloong.toolman.ui.screen.appdetails.AppDetailsScreen
import com.vsloong.toolman.ui.screen.appdetails.AppDetailsViewModel
import com.vsloong.toolman.ui.screen.channel.ChannelScreen
import com.vsloong.toolman.ui.themes.R
import com.vsloong.toolman.ui.widget.AppButton
import com.vsloong.toolman.ui.widget.AppTextFiled

class AppsScreen : BaseScreen {
    @Composable
    override fun Content() {

        val viewModel = rememberViewModel { AppsViewModel() }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SearchContent(
                onExecuteClick = {
                    viewModel.appsEvent.onSearchClick.invoke(it)
                }
            )


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.packages) {
                    AppItem(
                        packageName = it,
                        appOperateEvent = viewModel.appOperateEvent
                    )
                }
            }

        }
    }

    @Composable
    private fun AppItem(
        packageName: String,
        appOperateEvent: AppOperateEvent
    ) {
        val localNavigator = LocalNavigator.current

        ContextMenuArea(
            items = {
                listOf(
                    ContextMenuItem("清除数据") { appOperateEvent.onClearData.invoke(packageName) },
                    ContextMenuItem("卸载应用") { appOperateEvent.onUnInstall.invoke(packageName) },
                    ContextMenuItem("更多") {
                        localNavigator?.push(AppDetailsScreen(packageName = packageName))
                    },
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .heightIn(min = 40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color(0xFFFFF6DF))
                    .clickable {

                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = packageName)
            }
        }
    }

    @Composable
    private fun SearchContent(
        onExecuteClick: (String) -> Unit = {}
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            val cmd = remember { mutableStateOf("") }

            AppTextFiled(
                text = cmd.value,
                onValueChange = {
                    cmd.value = it
                },
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .height(40.dp)
            )

            AppButton(
                modifier = Modifier.height(40.dp),
                text = "搜索",
                onClick = {
                    onExecuteClick.invoke(cmd.value)
                    cmd.value = ""
                }
            )
        }
    }
}