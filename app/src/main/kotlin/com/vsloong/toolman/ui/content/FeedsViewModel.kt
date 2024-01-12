package com.vsloong.toolman.ui.content

import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.core.common.usecase.ApkSignerUseCase
import com.vsloong.toolman.manager.AssetsManager
import java.io.File

class FeedsViewModel(
    adbUseCase: AdbUseCase = AdbUseCase(assetsManager = AssetsManager),
    apkSignerUseCase: ApkSignerUseCase = ApkSignerUseCase(assetsManager = AssetsManager),
) : BaseViewModel() {


    val feedsEvent = FeedsEvent(
        onExecuteClick = {

            val packageName = "com.honeycam.lite"
            val apkInstallPath = adbUseCase.apkPath(packageName)
            val apkPath = apkInstallPath.replaceFirst("package:", "")

            val apkName = File(apkPath).name

            adbUseCase.shellCopy(
                apkInstallFilePath = apkPath,
                phoneCacheDirPath = WorkspaceManager.phoneCachePath.toString()
            )

            val localCachePath = WorkspaceManager.localCachePath.resolve(
                "${packageName}.apk"
            )
            adbUseCase.pull(
                deviceId = "988a5b35563658344f",
                remotePath = WorkspaceManager.phoneCachePath.resolve(apkName).toString(),
                localPath = localCachePath.toString()
            )

            apkSignerUseCase.verify(localCachePath)
        }
    )
}