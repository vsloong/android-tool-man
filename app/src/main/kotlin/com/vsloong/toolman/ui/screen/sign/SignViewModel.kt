package com.vsloong.toolman.ui.screen.sign

import androidx.compose.runtime.mutableStateOf
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.model.SignInfo
import com.vsloong.toolman.core.common.usecase.ApkSignerUseCase
import com.vsloong.toolman.core.common.usecase.ZipAlignUseCase
import com.vsloong.toolman.manager.AssetsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

class SignViewModel(
    private val zipAlignUseCase: ZipAlignUseCase = ZipAlignUseCase(iAssetsPath = AssetsManager),
    private val apkSignerUseCase: ApkSignerUseCase = ApkSignerUseCase(assetsManager = AssetsManager)
) : BaseViewModel() {

    val signEvent = SignEvent(
        onKeyStoreFileSelect = {
            keystoreFile.value = it
        },
        onApkFileSelect = {
            apkFile.value = it
            check()
        },
        onSignClick = {
            sign()
        }
    )

    val apkFile = mutableStateOf(Path(""))
    val keystoreFile = mutableStateOf(Path(""))
    val keystorePass = mutableStateOf("")
    val keyAlias = mutableStateOf("")
    val keyPass = mutableStateOf("")


    // 已签名的信息
    val signInfo = mutableStateOf(SignInfo())
    val signState = mutableStateOf<SignState>(SignState.Idle)

    /**
     * 检测签名
     */
    private fun check() {
        viewModelScope.launch(Dispatchers.IO) {

            signState.value = SignState.Checking

            val result = apkSignerUseCase.verify(apkFile = apkFile.value)

            if (result == null) {
                signState.value = SignState.UnSign
            } else {
                signState.value = SignState.Signed
                signInfo.value = result
            }
        }
    }

    /**
     * 签名
     */
    private fun sign() {
        viewModelScope.launch(Dispatchers.IO) {

            val sourceApkFile = apkFile.value

            // 先对齐
            val alignedApkFile = sourceApkFile.parent.resolve("${sourceApkFile.nameWithoutExtension}_aligned.apk")
            zipAlignUseCase.align(
                apkFile = sourceApkFile,
                outputApkFile = alignedApkFile
            )

            // 再签名
            val signedApkFile = alignedApkFile.parent.resolve("${alignedApkFile.nameWithoutExtension}_signed.apk")
            apkSignerUseCase.sign(
                apkFile = alignedApkFile,
                keystoreFile = keystoreFile.value,
                keystorePass = keystorePass.value,
                keyAlias = keyAlias.value,
                keyPass = keyPass.value,
                outputApkFile = signedApkFile
            )
        }
    }
}