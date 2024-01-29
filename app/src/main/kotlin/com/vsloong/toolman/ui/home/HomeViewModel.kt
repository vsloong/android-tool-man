package com.vsloong.toolman.ui.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.model.AdbDeviceInfo
import com.vsloong.toolman.core.common.model.DeviceWrapper
import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.core.common.usecase.BundleUseCase
import com.vsloong.toolman.core.common.utils.logger
import com.vsloong.toolman.manager.AssetsManager
import com.vsloong.toolman.manager.DeviceManager
import com.vsloong.toolman.ui.tab.LeftTabEvent
import com.vsloong.toolman.ui.tab.TabType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

class HomeViewModel(
    private val adbUseCase: AdbUseCase = AdbUseCase(assetsManager = AssetsManager),
    private val bundleUseCase: BundleUseCase = BundleUseCase(assetsManager = AssetsManager),
) : BaseViewModel() {

    val currentTab = mutableStateOf<TabType>(TabType.Feed)

    val tabEvent = LeftTabEvent(
        onFeatureClick = {
            currentTab.value = TabType.Feature
        },
        onHomeClick = {
            currentTab.value = TabType.Feed
        },
        onAppsClick = {
            currentTab.value = TabType.Apps
        },
        onChannelClick = {
            currentTab.value = TabType.Channel
        },
        onSignClick = {
            currentTab.value = TabType.Sign
        }
    )

    fun install(paths: List<Path>, devices: Set<DeviceWrapper>) {

        if (paths.size > 1) {
            logger("不支持多选文件哦")
            return
        }

        val path = paths.first()
        if (!Files.isRegularFile(path)) {
            logger("请选择.aab或者.apk文件")
            return
        }

        // 文件后缀
        when (val extension = path.extension) {
            // 安装APK
            "apk" -> {
                viewModelScope.launch(Dispatchers.IO) {
                    adbUseCase.installApk(
                        apkPath = path,
                        devices = devices
                            .map {
                                it.serialNumber
                            }
                            .toSet()
                    )
                }
            }

            // 安装AAB
            "aab" -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val outputPath = path.parent.resolve("sample.apks")
                    bundleUseCase.buildApks(aabPath = path, outputApksPath = outputPath)
                    bundleUseCase.installApks(
                        apksPath = outputPath,
                        devices = devices
                            .map {
                                it.serialNumber
                            }.toSet()
                    )
                }
            }

            // 其他暂不支持
            else -> {
                logger("不支持的文件类型：${extension}")
            }
        }

    }

    override fun onDispose() {
        super.onDispose()
        logger("HomeViewModel on Dispose")
    }
}