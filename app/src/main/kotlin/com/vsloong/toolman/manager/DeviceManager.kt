package com.vsloong.toolman.manager

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener
import com.android.ddmlib.IDevice
import com.vsloong.toolman.core.common.model.DeviceWrapper
import java.util.concurrent.TimeUnit

object DeviceManager {

    /**
     * 设备集合
     */
    val deviceMap = mutableStateMapOf<String, DeviceWrapper>()

    /**
     * 所有设备列表
     */
    fun deviceList() = deviceMap.values.toList()

    /**
     * 当前使用的设备
     */
    val currentDevice = mutableStateOf<DeviceWrapper?>(null)

    fun selectedDevice(): DeviceWrapper? {
        return currentDevice.value
    }

    fun setSelectedDevice(device: DeviceWrapper) {
        currentDevice.value = device
    }

    init {

        val adbPath = AssetsManager.getAdbPath()
        AndroidDebugBridge.addDeviceChangeListener(object : IDeviceChangeListener {
            override fun deviceConnected(device: IDevice) {
                val deviceWrapper = DeviceWrapper(device)
                currentDevice.value = deviceWrapper
                deviceMap[device.serialNumber] = deviceWrapper
            }

            override fun deviceDisconnected(device: IDevice) {
                deviceMap.remove(device.serialNumber)

                currentDevice.value = deviceMap.values.firstOrNull()
            }

            override fun deviceChanged(device: IDevice, changeMask: Int) {
            }
        })

        AndroidDebugBridge.init(false)
        AndroidDebugBridge.createBridge(adbPath.toString(), false, 5000, TimeUnit.MILLISECONDS)
    }
}