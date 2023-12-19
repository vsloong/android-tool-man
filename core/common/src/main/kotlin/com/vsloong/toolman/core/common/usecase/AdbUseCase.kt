package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.utils.exec
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class AdbUseCase(
    assetsManager: IAssetsPath
) {

    private val adbPath = assetsManager.getAdbPath().absolutePathString()

    fun help() {
        exec(adbPath, "--help")
    }

    fun devices() {
        exec(adbPath, "devices", "-l")
    }

    fun killServer() {
        exec(adbPath, "kill-server")
    }

    fun installApk(apkPath: Path) {
        exec(adbPath, "install", apkPath.toString())
    }
}