package com.vsloong.toolman.ui.content

import androidx.compose.runtime.mutableStateListOf
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.common.model.CmdOutput
import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.core.common.usecase.ApkSignerUseCase
import com.vsloong.toolman.core.common.usecase.factory.UseCaseFactory
import com.vsloong.toolman.manager.AssetsManager
import java.io.File

class FeedsViewModel(
    private val cmdUseCaseFactory: UseCaseFactory = UseCaseFactory(
        assetsManager = AssetsManager
    )
) : BaseViewModel() {


    val feedsEvent = FeedsEvent(
        onExecuteClick = { cmd ->

            val result = cmdUseCaseFactory.run(cmd)
            _cmdResultList.add(result)

//            val packageName = "com.honeycam.lite"
//            val apkInstallPath = adbUseCase.apkPath(packageName)
//            val apkPath = apkInstallPath.replaceFirst("package:", "")
//
//            val apkName = File(apkPath).name
//
//            adbUseCase.shellCopy(
//                apkInstallFilePath = apkPath,
//                phoneCacheDirPath = WorkspaceManager.phoneCachePath.toString()
//            )
//
//            val localCachePath = WorkspaceManager.localCachePath.resolve(
//                "${packageName}.apk"
//            )
//            adbUseCase.pull(
//                deviceId = "988a5b35563658344f",
//                remotePath = WorkspaceManager.phoneCachePath.resolve(apkName).toString(),
//                localPath = localCachePath.toString()
//            )
//
//            apkSignerUseCase.verify(localCachePath)
        }
    )

    private val _cmdResultList = mutableStateListOf<CmdOutput>()
    val cmdResultList: List<CmdOutput> = _cmdResultList
}