package com.vsloong.toolman.ui.screen.device

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.toolman.base.BaseTabOptions
import com.vsloong.toolman.base.BaseTabScreen
import com.vsloong.toolman.base.rememberViewModel
import com.vsloong.toolman.core.common.model.CmdOutput
import com.vsloong.toolman.ui.themes.R
import com.vsloong.toolman.ui.widget.AppButton
import com.vsloong.toolman.ui.widget.AppTextFiled

object DeviceTabScreen : BaseTabScreen() {

    override val tabOptions: BaseTabOptions
        get() = BaseTabOptions(
            title = "设备",
        )

    @Composable
    override fun Content() {
        val deviceViewModel = rememberViewModel { DeviceViewModel() }
        val lazyListState = rememberLazyListState()

        LaunchedEffect(key1 = Unit){
            deviceViewModel.checkDevice()
        }

        Row(modifier = Modifier.fillMaxSize()) {

            // 设备列表
            Box(
                modifier = Modifier.fillMaxHeight()
                    .width(180.dp)
            ) {

                if (deviceViewModel.deviceList().isEmpty()) {
                    Text(text = "当前没有设备连接哦")
                } else {
                    LazyColumn(modifier = Modifier.width(180.dp)) {
                        items(deviceViewModel.deviceList()) {
                            DeviceItem(mode = it.name)
                        }
                    }
                }
            }


            // 设备操作相关内容
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(color = Color(0xFFF5F5F5)),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                LaunchedEffect(deviceViewModel.cmdResultList.size) {
                    if (deviceViewModel.cmdResultList.size > 1) {
                        lazyListState.scrollToItem(deviceViewModel.cmdResultList.size - 1)
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize().weight(1f)
                        .clip(RoundedCornerShape(20.dp)),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    state = lazyListState
                ) {
                    itemsIndexed(deviceViewModel.cmdResultList) { index, item ->
                        CmdItem(item, index)
                    }
                }

                SendMessageContent(
                    onExecuteClick = deviceViewModel.deviceEvent.onExecuteClick,
                )
            }
        }
    }

    @Composable
    private fun CmdItem(cmdOutPut: CmdOutput, index: Int) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    color = if (index % 2 == 0) {
                        Color(0xFFDFEBFF)
                    } else {
                        Color(0xFFFFF6DF)
                    }
                )
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = cmdOutPut.cmd,
                color = R.colors.text_primary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = cmdOutPut.output,
                color = R.colors.text_primary,
                fontSize = 14.sp,
            )
        }
    }

    @Composable
    private fun SendMessageContent(
        onExecuteClick: (String) -> Unit = {}
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            val cmd = remember { mutableStateOf("") }

            AppTextFiled(
                text = cmd.value,
                onValueChange = {
                    cmd.value = it
                },
                modifier = Modifier.fillMaxWidth().height(40.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

//                FunctionItem()

                Spacer(modifier = Modifier.fillMaxWidth().weight(1f))

                AppButton(
                    modifier = Modifier.height(40.dp),
                    text = "执行",
                    onClick = {
                        onExecuteClick.invoke(cmd.value)
                        cmd.value = ""
                    }
                )
            }

        }
    }


    @Composable
    private fun DeviceItem(mode: String) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = mode, fontSize = 18.sp)
            Text(text = "这是设备序列号", fontSize = 14.sp)
        }
    }
}