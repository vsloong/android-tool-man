package com.vsloong.toolman.ui.screen.appdetails

import androidx.compose.runtime.mutableStateOf
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.common.model.SignInfo
import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.core.common.usecase.ApkSignerUseCase
import com.vsloong.toolman.core.common.utils.logger
import com.vsloong.toolman.manager.AssetsManager
import com.vsloong.toolman.manager.DeviceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.io.path.Path

class AppDetailsViewModel(
    private val packageName: String,
    private val adbUseCase: AdbUseCase = AdbUseCase(assetsManager = AssetsManager),
    private val apkSignerUseCase: ApkSignerUseCase = ApkSignerUseCase(assetsManager = AssetsManager),
    private val deviceManager: DeviceManager = DeviceManager
) : BaseViewModel() {


    val apkPullState = mutableStateOf<ApkDetailsState>(ApkDetailsState.Pulling)
    val localApkPath = mutableStateOf(Path(""))
    val signInfo = mutableStateOf<SignInfo?>(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {

            // 提取APK文件
            val serialNumber = deviceManager.selectedDevice()?.serialNumber ?: return@launch
            val path = adbUseCase.apkPath(deviceId = serialNumber, packageName = packageName)

            if (path.isEmpty()) {
                logger("未检测到APK文件")
                return@launch
            }

            val localPath = WorkspaceManager.getLocalServerDirPath().resolve("${packageName}.apk")
            localApkPath.value = localPath

            adbUseCase.pull(
                deviceId = serialNumber,
                remotePath = path,
                localPath = localPath.toString()
            )
            apkPullState.value = ApkDetailsState.PullDone

            // 检测签名
            apkPullState.value = ApkDetailsState.CheckSign
            signInfo.value = apkSignerUseCase.verify(localPath)
            apkPullState.value = ApkDetailsState.CheckSignDown
            println("APK的签名信息：$signInfo")
        }
    }
}