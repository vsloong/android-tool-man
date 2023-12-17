package com.vsloong.toolman.usecase

import com.vsloong.toolman.AppScope
import com.vsloong.toolman.manager.AssetsManager
import com.vsloong.toolman.utils.exec
import kotlin.io.path.absolutePathString

class AdbUseCase(
    assetsManager: AssetsManager = AssetsManager
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
}