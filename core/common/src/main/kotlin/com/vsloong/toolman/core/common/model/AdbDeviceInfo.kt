package com.vsloong.toolman.core.common.model


/**
 * adb 命令获取到的设备信息
 */
@Deprecated(message = "请使用DeviceWrapper")
data class AdbDeviceInfo(
    val deviceId: String,
    val model: String,
)
