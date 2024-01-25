package com.vsloong.toolman.ui.screen.feeds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.toolman.base.BaseScreen
import com.vsloong.toolman.base.rememberViewModel
import com.vsloong.toolman.core.common.model.CmdOutput
import com.vsloong.toolman.ui.themes.R
import com.vsloong.toolman.ui.widget.AppButton
import com.vsloong.toolman.ui.widget.AppTextFiled

class FeedsScreen : BaseScreen {

    @Composable
    override fun Content() {
        val feedsViewModel = rememberViewModel { FeedsViewModel() }
        val lazyListState = rememberLazyListState()

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            LaunchedEffect(feedsViewModel.cmdResultList.size) {
                if (feedsViewModel.cmdResultList.size > 1) {
                    lazyListState.scrollToItem(feedsViewModel.cmdResultList.size - 1)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f)
                    .clip(RoundedCornerShape(20.dp)),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = lazyListState
            ) {
                itemsIndexed(feedsViewModel.cmdResultList) { index, item ->
                    CmdItem(item, index)
                }
            }

            SendMessageContent(
                onExecuteClick = feedsViewModel.feedsEvent.onExecuteClick,
            )
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
    private fun FunctionItem() {
        Row {

            Image(
                painter = painterResource("icon_file.svg"),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Text(text = "文件")
        }
    }
}