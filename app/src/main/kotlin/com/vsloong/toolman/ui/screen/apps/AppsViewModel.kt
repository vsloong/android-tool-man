package com.vsloong.toolman.ui.screen.apps

import androidx.compose.runtime.mutableStateListOf
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.base.viewModelIoScope
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.core.common.usecase.ApkSignerUseCase
import com.vsloong.toolman.core.common.utils.logger
import com.vsloong.toolman.manager.AssetsManager
import com.vsloong.toolman.manager.DeviceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppsViewModel(
    private val adbUseCase: AdbUseCase = AdbUseCase(assetsManager = AssetsManager),
    private val deviceManager: DeviceManager = DeviceManager
) : BaseViewModel() {

    val cachePackages = mutableListOf<String>()
    val packages = mutableStateListOf<String>()

    init {
        queryApps()
    }

    val appsEvent = AppsEvent(
        onSearchClick = {
            searchPackage(it)
        }
    )

    val appOperateEvent = AppOperateEvent(
        onClearData = {
            viewModelScope.launch(Dispatchers.IO) {
                val serialNumber = deviceManager.selectedDevice()?.serialNumber ?: return@launch
                adbUseCase.clearData(deviceId = serialNumber, packageName = it)
            }
        },
        onUnInstall = {
            viewModelScope.launch(Dispatchers.IO) {
                val serialNumber = deviceManager.selectedDevice()?.serialNumber ?: return@launch
                adbUseCase.uninstall(deviceId = serialNumber, packageName = it)
                queryApps()
            }
        },
    )

    private fun queryApps() {
        viewModelScope.launch {

            val serialNumber = deviceManager.selectedDevice()?.serialNumber ?: return@launch

            val result = adbUseCase.packages(deviceId = serialNumber)

            cachePackages.clear()
            cachePackages.addAll(result)

            packages.clear()
            packages.addAll(result)
        }
    }

    /**
     * 查找应用
     */
    private fun searchPackage(packageName: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = packages.filter { it.contains(packageName) }

            packages.clear()
            packages.addAll(result)
        }
    }

}