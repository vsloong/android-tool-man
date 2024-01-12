package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.model.AdbDeviceInfo
import com.vsloong.toolman.core.common.utils.exec
import com.vsloong.toolman.core.common.utils.logger
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

/**
 * adb内容参考：https://developer.android.google.cn/tools/adb?hl=zh-cn
 */
class AdbUseCase(
    assetsManager: IAssetsPath,
    private val regexUseCase: RegexUseCase = RegexUseCase()
) {

    private val adbPath = assetsManager.getAdbPath().absolutePathString()

    /**
     * 空白字符串
     */

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

    /**
     * 获取当前Activity
     */
    fun currentFocusActivity(deviceId: String) {
        exec(
            cmd = "$adbPath -s $deviceId shell dumpsys window | grep mCurrentFocus",
            onLine = {
                logger("FocusActivity=$it")
            }
        )
    }

    /**
     * 启动Activity
     */
    fun startActivity(
        deviceId: String,
        applicationId: String,
        classPath: String
    ) {
        exec(
            cmd = "$adbPath -s $deviceId shell am start -n $applicationId/$classPath",
            onLine = {
            }
        )
    }

    fun getDeviceProductModel(deviceId: String) {
        exec(
            cmd = "$adbPath -s $deviceId shell getprop ro.product.model"
        )
    }

    fun getDeviceProductDevice(deviceId: String) {
        exec(
            cmd = "$adbPath -s $deviceId shell getprop ro.product.device"
        )
    }

    fun getDeviceSize(deviceId: String) {
        exec(
            cmd = "$adbPath -s $deviceId shell wm size"
        )
    }

    /**
     * 获取屏幕密度
     */
    fun getDeviceDensity(deviceId: String) {
        exec(
            cmd = "$adbPath -s $deviceId shell wm density"
        )
    }

    /**
     * 获取系统版本
     */
    fun getDeviceOsVersion(deviceId: String) {
        exec(
            cmd = "$adbPath -s $deviceId shell getprop ro.build.version.release"
        )
    }

    fun getDeviceCPU(deviceId: String) {
        exec(
            cmd = "$adbPath -s $deviceId shell cat /proc/cpuinfo"
        )
    }

    /**
     * 截图
     */
    fun screenCap(deviceId: String, deviceFile: String) {
        exec(cmd = "$adbPath -s $deviceId shell screencap $deviceFile")
    }

    /**
     * 将私有目录下的app拷贝到外部可访问目录
     */
    fun shellCopy(
        apkInstallFilePath: String, phoneCacheDirPath: String
    ) {

        checkPhonePath(phoneCacheDirPath)

        exec(cmd = "$adbPath shell cp -f $apkInstallFilePath $phoneCacheDirPath")
    }

    fun checkPhonePath(path: String) {
        val phonePath = Path(path)
//        val dir = if (Files.isDirectory(phonePath)) {
//            phonePath
//        } else {
//            phonePath.parent
//        }

        exec(cmd = "$adbPath shell mkdir $phonePath")
    }

    /**
     * 从手机拉取文件到本地
     *
     * @param deviceId 设备ID
     * @param remotePath 手机端的文件地址
     * @param localPath 电脑端的文件地址
     */
    fun pull(deviceId: String, remotePath: String, localPath: String) {
        val path = Path(localPath)

        val parent = path.parent
        if (!Files.exists(parent)) {
            Files.createDirectories(parent)
        }

        exec(cmd = "$adbPath -s $deviceId pull $remotePath $localPath")
    }

    /**
     * 从本地推送文件到手机
     */
    fun push(deviceId: String, localPath: String, remotePath: String) {
        exec(
            cmd = "$adbPath -s $deviceId push $localPath $remotePath"
        )
    }

    fun packages() {
        exec(cmd = "$adbPath shell pm list packages -f")
    }

    /**
     * 获取应用的安装包位置
     */
    fun apkPath(packageName: String): String {
        var apkPath = ""
        exec(
            cmd = "$adbPath shell pm path $packageName",
            onLine = {
                apkPath = it
            })
        return apkPath
    }

    fun getDevice() {
        exec(adbPath, "shell", "getprop", "ro.build.version.sdk")
        exec(adbPath, "shell", "getprop", "ro.build.version.release")
        exec(adbPath, "shell", "getprop", "ro.product.model")
        exec(adbPath, "shell", "getprop", "ro.product.brand")
        exec(adbPath, "shell", "getprop", "ro.product.name")
        exec(adbPath, "shell", "getprop", "ro.product.board")
        exec(adbPath, "shell", "getprop", "ro.product.cpu.abilist")
        exec(adbPath, "shell", "getprop", "ro.sf.Idc_density")
    }
}