package com.vsloong.toolman.core.common.test

import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.core.common.utils.logger
import javax.swing.filechooser.FileSystemView

fun main() {
    val adb = AdbUseCase(assetsManager = TestAssetsManager)

    adb.help()
    adb.version()

//    val devices = adb.getDevices()
//
//    var deviceId = ""
//    devices.forEach {
//        logger("获取的设备信息：$it")
//        deviceId = it.deviceId
//    }
//    adb.currentFocusActivity(deviceId = deviceId)
//
//    adb.packages()
//
//    adb.apkPath(packageName = "com.honeycam.lite")


//    adb.startActivity(
//        deviceId = deviceId,
//        applicationId = "com.honeycam",
//        classPath = "com.donglib.app.pay.GooglePayActivity",
//    )

//    adb.getDeviceProductModel(deviceId = deviceId)
//    adb.getDeviceProductDevice(deviceId = deviceId)
//    adb.getDeviceSize(deviceId = deviceId)
//    adb.getDeviceDensity(deviceId = deviceId)
//    adb.getDeviceOsVersion(deviceId = deviceId)
//    adb.getDeviceCPU(deviceId = deviceId)

//    val screenFile = "/sdcard/screen.png"
//    adb.screenCap(deviceId = deviceId, deviceFile = screenFile)
//
//    val desktopDirPath = FileSystemView.getFileSystemView().homeDirectory.toPath().resolve("Desktop")
//    logger("桌面路径：$desktopDirPath")

//
//    adb.pull(
//        deviceId = deviceId,
//        remotePath = screenFile,
//        localPath = desktopDirPath.resolve("screen996.png").toString()
//    )
//
//    adb.push(
//        deviceId = deviceId,
//        localPath = "/Users/dragon/Desktop/ic_launcher.png",
//        remotePath = "/sdcard/hello/ic_launcher.png"
//    )
//    adb.getDevice()


//    adb.installApk(
//        apkPath = Path("/Users/dragon/Temp/Bundle/output/universal.apk"),
//        devices = devices
//    )

    // 解析devices数据
//    adb.getDeviceInfo("988a5b35563658344f     device usb:36700160X product:dreamqltezc model:SM_G9500 device:dreamqltechn transport_id:4")


//    adb.installApk(Path("/Users/dragon/Temp/Bundle/output/universal.apk"))
}

