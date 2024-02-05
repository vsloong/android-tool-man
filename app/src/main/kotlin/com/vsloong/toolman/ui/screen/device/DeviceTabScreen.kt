package com.vsloong.toolman.ui.screen.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.toolman.base.BaseTabOptions
import com.vsloong.toolman.base.BaseTabScreen
import com.vsloong.toolman.base.rememberViewModel
import com.vsloong.toolman.core.common.model.CmdOutput
import com.vsloong.toolman.core.common.model.CmdOutputType
import com.vsloong.toolman.core.common.model.DeviceWrapper
import com.vsloong.toolman.ui.screen.feature.FeatureEvent
import com.vsloong.toolman.ui.screen.feature.ToolManFeature
import com.vsloong.toolman.ui.themes.R
import com.vsloong.toolman.ui.widget.AppTextFiled1
import java.io.File

object DeviceTabScreen : BaseTabScreen() {

    override val tabOptions: BaseTabOptions
        get() = BaseTabOptions(
            title = "设备",
        )

    @Composable
    override fun Content() {
        val deviceViewModel = rememberViewModel { DeviceViewModel() }
        val lazyListState = rememberLazyListState()

        LaunchedEffect(key1 = Unit) {
            deviceViewModel.checkDevice()
        }

        Row(modifier = Modifier.fillMaxSize()) {

            // 设备列表
            Box(
                modifier = Modifier.fillMaxHeight()
                    .background(color = Color.Black.copy(alpha = 0.01f))
                    .width(280.dp)
            ) {

                if (deviceViewModel.deviceList().isEmpty()) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            space = 12.dp,
                            alignment = Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource("default_empty.svg"),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth
                        )

                        Text(text = "当前没有设备哦")
                    }

                } else {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(deviceViewModel.deviceList()) {
                            DeviceItem(
                                device = it,
                                selected = it == deviceViewModel.currentDevice.value,
                                onClick = {
                                    deviceViewModel.setSelectedDevice(it)
                                }
                            )
                        }
                    }
                }
            }


            // 设备操作相关内容
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {

                LaunchedEffect(deviceViewModel.cmdResultList.size) {
                    if (deviceViewModel.cmdResultList.size > 1) {
                        lazyListState.scrollToItem(deviceViewModel.cmdResultList.size - 1)
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    state = lazyListState
                ) {
                    itemsIndexed(deviceViewModel.cmdResultList) { index, item ->
                        if (item.type is CmdOutputType.Image) {
                            CmdImageItem(item, index)
                        } else {
                            CmdTextItem(item, index)
                        }
                    }
                }

                FeatureContent(featureEvent = deviceViewModel.featureEvent)

                SendMessageContent(
                    onSend = deviceViewModel.deviceEvent.onExecuteClick,
                )
            }
        }
    }

    @Composable
    private fun CmdTextItem(cmdOutPut: CmdOutput, index: Int) {
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
    private fun CmdImageItem(cmdOutPut: CmdOutput, index: Int) {
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

            Image(
                bitmap = loadImageBitmap(File(cmdOutPut.output).inputStream()),
                contentDescription = null,
                modifier = Modifier.width(120.dp).clip(RoundedCornerShape(8.dp)),
            )
        }
    }

    @Composable
    private fun SendMessageContent(
        onSend: (String) -> Unit = {}
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            AppTextFiled1(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                onSend = onSend
            )
        }
    }


    @Composable
    private fun DeviceItem(
        device: DeviceWrapper,
        selected: Boolean,
        onClick: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (selected) {
                        Color.Black.copy(alpha = 0.08f)
                    } else {
                        Color.Transparent
                    }
                )
                .clickable {
                    onClick.invoke()
                }
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = device.getPhoneName(), fontSize = 18.sp)
            Text(text = device.serialNumber, fontSize = 14.sp)
        }
    }

    /**
     * 功能区域
     */
    @Composable
    private fun FeatureContent(featureEvent: FeatureEvent) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Divider(modifier = Modifier.fillMaxWidth(), color = Color.Black.copy(alpha = 0.06f))

            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureItem(
                    resourcePath = "feature_screen_cap.svg",
                    onClick = {
                        featureEvent.onFeatureClick.invoke(ToolManFeature.ScreenCap)
                    }
                )
                FeatureItem(
                    resourcePath = "feature_top_activity.svg",
                    onClick = {
                        featureEvent.onFeatureClick.invoke(ToolManFeature.TopActivity)
                    }
                )
            }
        }
    }


    @Composable
    private fun FeatureItem(
        resourcePath: String,
        onClick: () -> Unit
    ) {
        Image(
            painter = painterResource(resourcePath),
            contentDescription = null,
            modifier = Modifier.size(20.dp).clickable {
                onClick()
            }
        )
    }
}