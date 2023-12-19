package com.vsloong.toolman.core.common.test

import com.vsloong.toolman.core.common.usecase.AdbUseCase

fun main() {
    val adb = AdbUseCase(assetsManager = TestAssetsManager)

    adb.help()
    adb.devices()

//    adb.installApk(Path("/Users/dragon/Temp/Bundle/output/universal.apk"))
}