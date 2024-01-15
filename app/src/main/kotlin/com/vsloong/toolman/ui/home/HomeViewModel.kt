package com.vsloong.toolman.ui.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.model.AdbDeviceInfo
import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.core.common.usecase.BundleUseCase
import com.vsloong.toolman.core.common.utils.logger
import com.vsloong.toolman.manager.AssetsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

class HomeViewModel(
    private val adbUseCase: AdbUseCase = AdbUseCase(assetsManager = AssetsManager),
    private val bundleUseCase: BundleUseCase = BundleUseCase(assetsManager = AssetsManager)
) : BaseViewModel() {

    val devices = mutableStateListOf<AdbDeviceInfo>()

    init {
        refreshDevices()
    }


    private val selectDevices = mutableSetOf<AdbDeviceInfo>()
    val homeEvent = HomeEvent(
        onDeviceSelect = {
            selectDevices.add(it)
        },
        onDeviceRemove = {
            selectDevices.remove(it)
        },
        onClearAllDevice = {
            selectDevices.clear()
        }
    )

    fun refreshDevices() {
        viewModelScope.launch(Dispatchers.IO) {
            devices.clear()
            devices.addAll(adbUseCase.getDevices())
        }
    }

    fun install(paths: List<Path>, devices: Set<AdbDeviceInfo>) {

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
                                it.deviceId
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
                                it.deviceId
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