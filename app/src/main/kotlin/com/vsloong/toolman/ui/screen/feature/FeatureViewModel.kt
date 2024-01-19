package com.vsloong.toolman.ui.screen.feature

import androidx.compose.runtime.mutableStateOf
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.manager.AssetsManager
import com.vsloong.toolman.manager.DeviceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeatureViewModel(
    private val adbUseCase: AdbUseCase = AdbUseCase(assetsManager = AssetsManager),
    private val deviceManager: DeviceManager = DeviceManager
) : BaseViewModel() {

    val featureEvent = FeatureEvent(
        onFeatureClick = {
            viewModelScope.launch(Dispatchers.IO) {
                when (it) {
                    is ToolManFeature.ScreenCap -> {
                        val fileName = "snapshot_${System.currentTimeMillis()}.png"
                        deviceManager.devices.first().let { deviceInfo ->
                            val deviceId = deviceInfo.deviceId

                            val deviceFile = WorkspaceManager.phoneCachePath.resolve(fileName)
                            adbUseCase.screenCap(deviceId = deviceId, deviceFile = deviceFile.toString())

                            val localFile = WorkspaceManager.localCachePath.resolve(fileName)
                            adbUseCase.pull(
                                deviceId = deviceId,
                                remotePath = deviceFile.toString(),
                                localPath = localFile.toString()
                            )

                            screenCap.value = localFile.toString()
                        }
                    }
                }
            }

        }
    )

    val screenCap = mutableStateOf("")

}