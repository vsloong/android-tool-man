package com.vsloong.toolman.ui.screen.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.vsloong.toolman.base.BaseScreen
import com.vsloong.toolman.base.BaseTabOptions
import com.vsloong.toolman.base.BaseTabScreen
import com.vsloong.toolman.base.rememberViewModel
import com.vsloong.toolman.ui.widget.AppButton
import com.vsloong.toolman.ui.widget.AppTextFiled
import com.vsloong.toolman.ui.widget.DragAndDropBox
import com.vsloong.toolman.ui.widget.ext.dashBorder

object ChannelTabScreen : BaseTabScreen() {

    override val tabOptions: BaseTabOptions
        get() = BaseTabOptions(
            title = "渠道",
            icon = "home_tab_channel.svg"
        )

    @Composable
    override fun Content() {

        val viewModel = rememberViewModel { ChannelViewModel() }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(12.dp)
        ) {


            // 工具类型
            Row(modifier = Modifier.clip(RoundedCornerShape(12.dp))) {
                Row() {
                    viewModel.channelToolType.forEach {
                        ToolItem(
                            toolType = it,
                            isSelect = it == viewModel.selectChannelToolType.value,
                            onToolClick = {
                                viewModel.selectChannelToolType.value = it
                            }
                        )
                    }
                }
            }

            DragAndDropBox(
                modifier = Modifier.fillMaxWidth()
                    .height(100.dp)
                    .dashBorder(color = Color.Black),
                onDrop = {
                    viewModel.channelEvent.onSelectApk.invoke(it)
                },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "拖拽APK文件到此")
            }

            InputContent(
                onPutChannelClick = {
                    viewModel.channelEvent.onPutChannel.invoke(it)
                },
                onGetChannelClick = {
                    viewModel.channelEvent.onGetChannel.invoke()
                },
                onRemoveChannelClick = {
                    viewModel.channelEvent.onRemoveChanel.invoke()
                }
            )

            Text(text = "查询渠道信息：${viewModel.currentChannel.value}")
        }
    }


    @Composable
    private fun ToolItem(
        toolType: String,
        isSelect: Boolean,
        onToolClick: () -> Unit,
    ) {
        Box(
            modifier = Modifier.width(100.dp)
                .height(50.dp)
                .background(
                    color = if (isSelect) {
                        Color.Black
                    } else {
                        Color(0xFFF3F1F1)
                    }
                )
                .clickable {
                    onToolClick.invoke()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = toolType,
                color = if (isSelect) {
                    Color.White
                } else {
                    Color.Black
                }
            )
        }
    }


    @Composable
    private fun InputContent(
        onPutChannelClick: (String) -> Unit,
        onGetChannelClick: () -> Unit,
        onRemoveChannelClick: () -> Unit,
    ) {

        val channel = remember { mutableStateOf("") }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppTextFiled(
                text = channel.value,
                onValueChange = {
                    channel.value = it
                },
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color(0xFFF3F1F1))
            )

            AppButton(
                modifier = Modifier.height(40.dp),
                text = "添加渠道",
                onClick = {
                    onPutChannelClick.invoke(channel.value)
                    channel.value = ""
                }
            )

            AppButton(
                modifier = Modifier.height(40.dp),
                text = "查询渠道",
                onClick = {
                    onGetChannelClick.invoke()
                }
            )

            AppButton(
                modifier = Modifier.height(40.dp),
                text = "删除渠道",
                onClick = {
                    onRemoveChannelClick.invoke()
                }
            )
        }
    }


}