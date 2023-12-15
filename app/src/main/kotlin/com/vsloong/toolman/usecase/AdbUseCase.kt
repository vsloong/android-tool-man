package com.vsloong.toolman.usecase

import com.vsloong.toolman.AppScope
import com.vsloong.toolman.utils.exec
import kotlin.io.path.absolutePathString

class AdbUseCase(
    private val assetsUseCase: AssetsUseCase
) {

    private val adbPath = assetsUseCase.getAdbPath().absolutePathString()
    fun help() {
        execute(adbPath, "--help")
    }

    fun devices() {
        execute(adbPath, "devices")
    }

    private fun execute(
        vararg cmd: String,
    ) {
        AppScope.launch { exec(*cmd) }
    }

}