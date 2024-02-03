package com.vsloong.toolman.ui.screen.device

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.model.CmdOutput
import com.vsloong.toolman.core.common.model.DeviceWrapper
import com.vsloong.toolman.core.common.usecase.factory.UseCaseFactory
import com.vsloong.toolman.core.common.utils.logger
import com.vsloong.toolman.manager.AssetsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class DeviceViewModel(
    private val cmdUseCaseFactory: UseCaseFactory = UseCaseFactory(
        assetsManager = AssetsManager
    )
) : BaseViewModel() {

    private var isAdbInit = false

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

    fun checkDevice() {
        if (isAdbInit) {
            return
        }
        isAdbInit = true

        viewModelScope.launch(Dispatchers.IO) {

            val adbPath = AssetsManager.getAdbPath()
            AndroidDebugBridge.addDeviceChangeListener(object : AndroidDebugBridge.IDeviceChangeListener {
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


    val deviceEvent = DeviceEvent(
        onExecuteClick = { cmd ->
            val result = cmdUseCaseFactory.run(cmd)
            _cmdResultList.add(result)
        }
    )

    private val _cmdResultList = mutableStateListOf<CmdOutput>()
    val cmdResultList: List<CmdOutput> = _cmdResultList
}