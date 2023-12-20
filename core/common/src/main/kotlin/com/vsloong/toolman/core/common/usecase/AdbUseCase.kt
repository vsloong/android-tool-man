package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.model.AdbDeviceInfo
import com.vsloong.toolman.core.common.utils.exec
import com.vsloong.toolman.core.common.utils.logger
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class AdbUseCase(
    assetsManager: IAssetsPath,
    private val regexUseCase: RegexUseCase = RegexUseCase()
) {

    private val adbPath = assetsManager.getAdbPath().absolutePathString()

    fun help() {
        exec(adbPath, "--help")
    }

    /**
     * 获取当前连接的设备信息
     */
    fun getDevices(): Set<AdbDeviceInfo> {
        val devices = mutableSetOf<AdbDeviceInfo>()
        exec(
            adbPath, "devices", "-l",
            onLines = {
                if (it.size <= 1) {
                    logger("没有设备连接")
                } else {
                    val list = it.subList(1, it.size)
                    list.forEach { line ->
                        val id = regexUseCase.getDeviceId(line)
                        val model = regexUseCase.getDeviceModel(line)

                        if (!id.isNullOrBlank() && !model.isNullOrBlank()) {
                            devices.add(AdbDeviceInfo(deviceId = id, model = model))
                        }
                    }
                }
            }
        )

        return devices
    }


    /**
     * 安装apk文件到设备
     */
    fun installApk(apkPath: Path, devices: Set<String>) {
        devices.forEach { device ->
            exec(
                adbPath,
                "-s",
                device,
                "install",
                apkPath.toString()
            )
        }
    }

    fun killServer() {
        exec(adbPath, "kill-server")
    }
}