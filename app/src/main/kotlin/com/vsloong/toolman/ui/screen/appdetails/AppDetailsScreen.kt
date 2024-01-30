package com.vsloong.toolman.ui.screen.appdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.vsloong.toolman.base.BaseScreen
import com.vsloong.toolman.core.common.model.SignInfo
import com.vsloong.toolman.ui.widget.AppProgressBar

class AppDetailsScreen(val packageName: String) : BaseScreen {

    @Composable
    override fun Content() {
        val viewModel = remember { AppDetailsViewModel(packageName) }

        val localNavigator = LocalNavigator.current

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource("ic_back.svg"),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp).clip(CircleShape)
                        .clickable {
                            localNavigator?.pop()
                        }
                )

                Text(text = packageName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            if (viewModel.apkPullState.value is ApkDetailsState.CheckSignDown) {
                AppSignInfo(
                    signInfo = viewModel.signInfo.value
                )
            } else {
                Column {
                    Text(
                        text = if (viewModel.apkPullState.value is ApkDetailsState.Pulling) {
                            "正在提取APK文件"
                        } else if (viewModel.apkPullState.value is ApkDetailsState.CheckSign) {
                            "正在检测签名数据"
                        } else {
                            ""
                        }
                    )

                    AppProgressBar(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color.LightGray,
                        color = Color.Black
                    )
                }
            }
        }


    }

    @Composable
    private fun AppSignInfo(signInfo: SignInfo?) {
        SelectionContainer {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (signInfo == null) {
                    Text(text = "未检测到该应用的签名信息")
                } else {

                    Text(text = "md5 = ${signInfo.md5}")
                    Text(text = "sha1 = ${signInfo.sha1}")
                    Text(text = "sha256 = ${signInfo.sha256}")
                }
            }
        }
    }

}