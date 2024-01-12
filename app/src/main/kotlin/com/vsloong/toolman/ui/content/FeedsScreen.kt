package com.vsloong.toolman.ui.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vsloong.toolman.base.BaseScreen
import com.vsloong.toolman.base.rememberViewModel

class FeedsScreen : BaseScreen {

    @Composable
    override fun Content() {
        val feedsViewModel = rememberViewModel { FeedsViewModel() }
        SendMessageContent(
            onExecuteClick = feedsViewModel.feedsEvent.onExecuteClick
        )
    }


    @Composable
    private fun SendMessageContent(
        onExecuteClick: () -> Unit = {}
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
                .background(color = Color(0xFFF3F1F1))
        ) {

            TextField(value = "请输入文本", onValueChange = {})

            Row {

                FunctionItem()

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(color = Color.Black)
                        .clickable {
                            onExecuteClick.invoke()
                        }
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                ) {
                    Text(text = "执行", color = Color.White)
                }
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