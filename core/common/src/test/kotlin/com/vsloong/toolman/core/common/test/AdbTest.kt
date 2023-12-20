package com.vsloong.toolman.core.common.test

import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.core.common.utils.logger
import kotlin.io.path.Path

fun main() {
    val adb = AdbUseCase(assetsManager = TestAssetsManager)

//    adb.help()
    val devices = adb.getDevices()

    devices.forEach {
        logger("获取的设备信息：$it")
    }

//    adb.installApk(
//        apkPath = Path("/Users/dragon/Temp/Bundle/output/universal.apk"),
//        devices = devices
//    )

    // 解析devices数据
//    adb.getDeviceInfo("988a5b35563658344f     device usb:36700160X product:dreamqltezc model:SM_G9500 device:dreamqltechn transport_id:4")


//    adb.installApk(Path("/Users/dragon/Temp/Bundle/output/universal.apk"))
}

