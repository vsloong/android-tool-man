package com.vsloong.toolman.ui.screen.feature

import androidx.compose.runtime.mutableStateOf
import com.android.ddmlib.IShellOutputReceiver
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.core.common.utils.logger
import com.vsloong.toolman.manager.AssetsManager
import com.vsloong.toolman.manager.DeviceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.awt.image.RenderedImage
import javax.imageio.ImageIO

class FeatureViewModel(
    private val adbUseCase: AdbUseCase = AdbUseCase(assetsManager = AssetsManager),
    private val deviceManager: DeviceManager = DeviceManager
) : BaseViewModel() {

    val featureEvent = FeatureEvent(
        onFeatureClick = {
            viewModelScope.launch(Dispatchers.IO) {
                when (it) {
                    is ToolManFeature.ScreenCap -> {
                        try {
                            deviceManager.selectedDevice()?.let {

                                val bufferedImage = it.screenshot.asBufferedImage()
                                val fileName = "snapshot_${System.currentTimeMillis()}.png"
                                val screenshotImagePath = WorkspaceManager.getLocalCacheDirPath().resolve(fileName)
                                ImageIO.write(bufferedImage as RenderedImage, "PNG", screenshotImagePath.toFile())

                                screenCap.value = screenshotImagePath.toString()
                            }
                        } catch (_: Throwable) {

                        }

                    }

                    ToolManFeature.TopActivity -> {

                    }
                }
            }

        }
    )

    val screenCap = mutableStateOf("")

    /**
     * 当前应用及Activity
     */
    fun currentFocusActivity() {
        viewModelScope.launch(Dispatchers.IO) {
            deviceManager.selectedDevice()?.executeShellCommand(
                "dumpsys window | grep mCurrentFocus",
                object : IShellOutputReceiver {
                    override fun addOutput(data: ByteArray?, offset: Int, length: Int) {
                        data ?: return
                        val str = String(data)
                        logger("命令执行输出结果：$str")
                    }

                    override fun flush() {
                    }

                    override fun isCancelled(): Boolean {
                        return false
                    }

                })

        }
    }

}