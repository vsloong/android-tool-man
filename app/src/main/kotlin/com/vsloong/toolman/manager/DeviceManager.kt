package com.vsloong.toolman.manager

import androidx.compose.runtime.mutableStateListOf
import com.vsloong.toolman.core.common.model.AdbDeviceInfo

object DeviceManager {

    val devices = mutableStateListOf<AdbDeviceInfo>()

    fun addDevices(list: Set<AdbDeviceInfo>){
        devices.clear()
        devices.addAll(list)
    }
}