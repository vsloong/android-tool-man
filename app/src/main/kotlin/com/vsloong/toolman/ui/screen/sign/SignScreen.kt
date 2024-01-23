package com.vsloong.toolman.ui.screen.sign

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vsloong.toolman.base.BaseScreen
import com.vsloong.toolman.base.rememberViewModel
import com.vsloong.toolman.core.common.model.SignInfo
import com.vsloong.toolman.ui.widget.AppButton
import com.vsloong.toolman.ui.widget.AppProgressBar
import com.vsloong.toolman.ui.widget.AppTextFiled
import com.vsloong.toolman.ui.widget.DragAndDropBox
import com.vsloong.toolman.ui.widget.ext.dashBorder

class SignScreen : BaseScreen {

    @Composable
    override fun Content() {
        val viewModel = rememberViewModel { SignViewModel() }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            DragAndDropBox(
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = 160.dp)
                    .dashBorder(color = Color.Black),
                onDrop = {
                    viewModel.signEvent.onApkFileSelect.invoke(it.first())
                },
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(text = "拖拽APK文件到此")
                    Text(text = viewModel.apkFile.value.toString())
                }
            }

            // 如果有签名，显示签名信息，否则展示签名功能区域
            if (viewModel.signState.value is SignState.Signed) {
                SignInfoContent(
                    signInfo = viewModel.signInfo.value,
                    onResignClick = viewModel.signEvent.onResignClick
                )
            } else if (viewModel.signState.value is SignState.UnSign) {
                UnsignInfoContent(
                    onResignClick = viewModel.signEvent.onResignClick
                )
            } else if (viewModel.signState.value is SignState.NeedSign) {
                ExecuteSignContent(
                    signEvent = viewModel.signEvent,
                    keystoreFile = viewModel.keystoreFile.value.toString(),
                    keystorePass = viewModel.keystorePass,
                    keyAlias = viewModel.keyAlias,
                    keyPass = viewModel.keyPass
                )
            } else if (viewModel.signState.value is SignState.Checking) {
                Text(text = "正在检测签名")
                AppProgressBar(
                    modifier = Modifier.fillMaxWidth()
                        .height(4.dp),
                    color = Color.Black,
                    backgroundColor = Color.LightGray
                )
            }
        }
    }


    /**
     * 签名信息
     */
    @Composable
    private fun SignInfoContent(
        signInfo: SignInfo,
        onResignClick: () -> Unit,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "该APK文件已签名")

            SelectionContainer {
                Text(
                    text = "md5=${signInfo.md5}\n" +
                            "sha1=${signInfo.sha1}\n" +
                            "sha256=${signInfo.sha256}"
                )
            }

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "重新签名",
                onClick = onResignClick
            )
        }
    }

    /**
     * 签名信息
     */
    @Composable
    private fun UnsignInfoContent(
        onResignClick: () -> Unit,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "该文件未签名"
            )

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "去签名",
                onClick = onResignClick
            )
        }
    }

    /**
     * 处理签名区域
     */
    @Composable
    private fun ExecuteSignContent(
        signEvent: SignEvent,
        keystoreFile: String,
        keystorePass: MutableState<String>,
        keyAlias: MutableState<String>,
        keyPass: MutableState<String>,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            DragAndDropBox(
                modifier = Modifier.fillMaxWidth()
                    .height(100.dp)
                    .dashBorder(color = Color.Black),
                onDrop = {
                    signEvent.onKeyStoreFileSelect.invoke(it.first())
                },
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(text = "拖拽签名文件到此")
                    Text(text = keystoreFile)
                }
            }

            AppTextFiled(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                text = keystorePass.value,
                hint = "请输入keystorePass",
                onValueChange = {
                    keystorePass.value = it
                }
            )

            AppTextFiled(
                modifier = Modifier.fillMaxWidth()
                    .height(46.dp),
                text = keyAlias.value,
                hint = "请输入keyAlias",
                onValueChange = {
                    keyAlias.value = it
                }
            )

            AppTextFiled(
                modifier = Modifier.fillMaxWidth()
                    .height(46.dp),
                text = keyPass.value,
                hint = "请输入keyPass",
                onValueChange = {
                    keyPass.value = it
                }
            )

            AppButton(
                modifier = Modifier.fillMaxWidth()
                    .height(46.dp),
                text = "签名",
                onClick = {
                    signEvent.onSignClick()
                }
            )
        }
    }
}