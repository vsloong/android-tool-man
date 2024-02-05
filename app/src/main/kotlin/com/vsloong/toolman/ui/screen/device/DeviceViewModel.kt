package com.vsloong.toolman.ui.screen.device

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.android.ddmlib.IShellOutputReceiver
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.common.model.CmdOutput
import com.vsloong.toolman.core.common.model.CmdOutputType
import com.vsloong.toolman.core.common.model.DeviceWrapper
import com.vsloong.toolman.core.common.usecase.factory.UseCaseFactory
import com.vsloong.toolman.core.common.utils.logger
import com.vsloong.toolman.manager.AssetsManager
import com.vsloong.toolman.ui.screen.feature.FeatureEvent
import com.vsloong.toolman.ui.screen.feature.ToolManFeature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.image.RenderedImage
import java.nio.file.Files
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO
import kotlin.io.path.extension
import kotlin.io.path.name

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
            viewModelScope.launch(Dispatchers.IO) {
                val result = cmdUseCaseFactory.run(cmd)
                _cmdResultList.add(result)
            }
        },
        onInstall = { paths ->
            currentDevice.value?.let { current ->
                viewModelScope.launch(Dispatchers.IO) {
                    paths
                        .filter {
                            it.name.endsWith(".apk") || it.endsWith(".aab")
                        }
                        .forEach { path ->
                            when (val extension = path.extension) {
                                // 安装APK
                                "apk" -> {
                                    cmdUseCaseFactory.getAdbUseCase().installApk(
                                        apkPath = path,
                                        deviceId = current.serialNumber
                                    )

                                }

                                // 安装AAB
                                "aab" -> {
                                    val bundleUseCase = cmdUseCaseFactory.getBundleUseCase()
                                    val outputPath = path.parent.resolve("sample.apks")
                                    bundleUseCase.buildApks(aabPath = path, outputApksPath = outputPath)
                                    bundleUseCase.installApks(
                                        apksPath = outputPath,
                                        deviceId = current.serialNumber
                                    )
                                }

                                // 其他暂不支持
                                else -> {

                                }
                            }
                        }
                }
            }
        }
    )

    val featureEvent: FeatureEvent = FeatureEvent { feature ->
        currentDevice.value?.let { device ->
            viewModelScope.launch(Dispatchers.IO) {
                when (feature) {
                    is ToolManFeature.ScreenCap -> {
                        val bufferedImage = device.screenshot.asBufferedImage()
                        val fileName = "snapshot_${System.currentTimeMillis()}.png"
                        val screenshotImagePath = WorkspaceManager.getLocalCacheDirPath().resolve(fileName)
                        ImageIO.write(bufferedImage as RenderedImage, "PNG", screenshotImagePath.toFile())
                        _cmdResultList.add(
                            CmdOutput(
                                cmd = "adb shell screencap -p $screenshotImagePath",
                                output = screenshotImagePath.toString(),
                                type = CmdOutputType.Image,
                            )
                        )
                    }

                    is ToolManFeature.TopActivity -> {
                        val result =
                            cmdUseCaseFactory.getAdbUseCase().currentFocusActivity(deviceId = device.serialNumber)
                        _cmdResultList.add(
                            CmdOutput(
                                cmd = "adb shell dumpsys window | grep mCurrentFocus",
                                output = result,
                            )
                        )
                    }
                }
            }


        }
    }

    private val _cmdResultList = mutableStateListOf<CmdOutput>()
    val cmdResultList: List<CmdOutput> = _cmdResultList
}