package com.vsloong.toolman.ui.screen.sign

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.toolman.base.BaseScreen
import com.vsloong.toolman.base.BaseTabOptions
import com.vsloong.toolman.base.BaseTabScreen
import com.vsloong.toolman.base.rememberViewModel
import com.vsloong.toolman.core.common.model.KeystoreModel
import com.vsloong.toolman.core.common.model.SignInfo
import com.vsloong.toolman.ui.dialog.KeystoreConfigDialog
import com.vsloong.toolman.ui.dialog.QrCodeDialog
import com.vsloong.toolman.ui.widget.AppButton
import com.vsloong.toolman.ui.widget.AppProgressBar
import com.vsloong.toolman.ui.widget.DragAndDropBox
import com.vsloong.toolman.ui.widget.ext.dashBorder
import kotlinx.coroutines.launch
import java.nio.file.Path

object SignTabScreen : BaseTabScreen() {

    override val tabOptions: BaseTabOptions
        get() = BaseTabOptions(
            title = "签名",
        )

    @Composable
    override fun Content() {

        val signViewModel = rememberViewModel { SignViewModel() }
        val keystoreViewModel = rememberViewModel { KeystoreViewModel() }


        val hostState = remember {
            SnackbarHostState()
        }


        LaunchedEffect(
            key1 = "Toast"
        ) {
            keystoreViewModel.toastEvent.collect() {
                hostState.showSnackbar(message = it)
            }
        }

        // 签名信息配置弹窗
        KeystoreConfigDialog(
            visible = keystoreViewModel.showKeystoreConfigDialog.value,
            onSaveClick = keystoreViewModel.keystoreEvent.onSaveKeystoreInfo,
            onCancelClick = {
                keystoreViewModel.showKeystoreConfigDialog.value = false
            }
        )

        // 二维码弹窗
        QrCodeDialog(
            qrCodePath = signViewModel.qrCodeImagePath.value,
            visible = signViewModel.showQrCodeDialog.value,
            onDismissClick = {
                signViewModel.showQrCodeDialog.value = false
            }
        )


        Box(modifier = Modifier.fillMaxSize()) {


            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "签名信息（${keystoreViewModel.keystoreListInfo.size - 1}）",
                    fontSize = 20.sp,
                    color = Color.Black
                )

                val scrollState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()

                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                        .draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { delta ->
                                coroutineScope.launch {
                                    scrollState.scrollBy(-delta)
                                }
                            },
                        ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    state = scrollState
                ) {
                    itemsIndexed(keystoreViewModel.keystoreListInfo) { index, model ->
                        if (index == 0) {
                            KeystoreInfoItem(
                                onClick = {
                                    keystoreViewModel.showKeystoreConfigDialog.value = true
                                }
                            )
                        } else {
                            KeystoreInfoItem(
                                keystoreModel = model,
                                isSelect = model == signViewModel.selectKeystoreModel.value,
                                onSelectKeystoreModel = {
                                    signViewModel.signEvent.onSelectKeystoreModel.invoke(model)
                                }
                            )
                        }

                    }
                }

                Text(text = "应用签名", fontSize = 20.sp, color = Color.Black)

                DragAndDropBox(
                    modifier = Modifier.fillMaxWidth()
                        .heightIn(min = 120.dp)
                        .dashBorder(color = Color.Black),
                    onDrop = {
                        signViewModel.signEvent.onApkFileSelect.invoke(it.first())
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Text(text = "拖拽APK文件到此")
                        Text(text = signViewModel.apkFile.value.toString())
                    }
                }

                // 如果有签名，显示签名信息，否则展示签名功能区域
                when (signViewModel.signState.value) {
                    is SignState.Signed -> {
                        SignInfoContent(
                            signInfo = signViewModel.signInfo.value,
                            onResignClick = signViewModel.signEvent.onResignClick
                        )
                    }

                    is SignState.UnSign -> {
                        UnsignInfoContent(
                            onResignClick = signViewModel.signEvent.onResignClick
                        )
                    }

                    is SignState.Checking -> {
                        Text(text = "正在检测签名")
                        AppProgressBar(
                            modifier = Modifier.fillMaxWidth()
                                .height(4.dp),
                            color = Color.Black,
                            backgroundColor = Color(0xFFF3F1F1)
                        )
                    }
                }

                OutputSignedApkInfo(
                    apkPath = signViewModel.outputSignedApkPath.value,
                    onShowQrCodeClick = {
                        signViewModel.signEvent.onShowQrCode.invoke(signViewModel.outputSignedApkPath.value)
                    }
                )
            }

            // Toast
            SnackbarHost(hostState = hostState, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    @Composable
    private fun OutputSignedApkInfo(
        apkPath: Path,
        onShowQrCodeClick: () -> Unit
    ) {
        if (apkPath.toString().isNotBlank()) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "输出APK地址：$apkPath")

                AppButton(
                    modifier = Modifier.width(400.dp),
                    text = "扫码下载安装",
                    onClick = {
                        onShowQrCodeClick.invoke()
                    })
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


    @Composable
    private fun KeystoreInfoItem(
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier.width(200.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Color(0xFFF3F1F1)
                )
                .clickable {
                    onClick.invoke()
                },
            verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource("ic_add.svg"),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Text(text = "添加签名数据")
        }
    }

    @Composable
    private fun KeystoreInfoItem(
        keystoreModel: KeystoreModel,
        isSelect: Boolean,
        onSelectKeystoreModel: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .width(200.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = if (isSelect) {
                        Color.Black
                    } else {
                        Color(0xFFF3F1F1)
                    }
                )
                .clickable {
                    onSelectKeystoreModel.invoke()
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically)
        ) {
            Text(
                text = keystoreModel.keyAlias,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelect) {
                    Color.White
                } else {
                    Color.Black
                }
            )

            Text(
                text = keystoreModel.keystoreFileName,
                color = if (isSelect) {
                    Color.White
                } else {
                    Color.Black
                }
            )
        }
    }
}