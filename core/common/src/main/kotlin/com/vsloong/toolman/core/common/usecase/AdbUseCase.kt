package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.common.model.AdbDeviceInfo
import com.vsloong.toolman.core.common.model.CmdOutput
import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase
import com.vsloong.toolman.core.common.utils.exec
import com.vsloong.toolman.core.common.utils.logger
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * adb内容参考：https://developer.android.google.cn/tools/adb?hl=zh-cn
 */
class AdbUseCase(
    private val assetsManager: IAssetsPath,
    private val regexUseCase: RegexUseCase = RegexUseCase()
) : ICmdUseCase {

    private val adbPath = assetsManager.getAdbPath()

    override fun cmdName(): String {
        return CmdType.Adb.cmdName
    }

    override fun cmdPath(): String {
        return assetsManager.getAdbPath().toString()
    }

    init {
        val path = WorkspaceManager.phoneCachePath
        mkdir(path = path.parent.toString())
        mkdir(path = path.toString())
    }

    /**
     * 空白字符串
     */

    fun help() {
        run("adb --help")
    }

    fun version() {
        run(cmd = "${cmdName()} version")
    }

    /**
     * 获取当前连接的设备信息
     */
    fun getDevices(): Set<AdbDeviceInfo> {
        val devices = mutableSetOf<AdbDeviceInfo>()
        exec(
            adbPath.toString(), "devices", "-l",
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
        if (devices.isEmpty()) {
            run(cmd = "${cmdName()} install $apkPath")
        } else {
            devices.forEach { device ->
                run(cmd = "${cmdName()} -s $device install $apkPath")
            }
        }
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
        exec(cmd = "$adbPath -s $deviceId shell screencap -p $deviceFile")
    }

    /**
     * 将私有目录下的app拷贝到外部可访问目录
     */
    fun shellCopy(
        apkInstallFilePath: String, phoneCacheDirPath: String
    ) {
        exec(cmd = "$adbPath shell cp -f $apkInstallFilePath $phoneCacheDirPath")
    }

    fun mkdir(path: String) {
        val phonePath = Path(path)
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

    /**
     * 获取三方应用
     */
    fun packages(deviceId: String): List<String> {
        val output = run(cmd = "${cmdName()} -s $deviceId shell pm list packages -3")

        return output.output.split("package:")
            .map {
                it.trim()
            }
            .filter {
                it.isNotBlank()
            }
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
        exec(adbPath.toString(), "shell", "getprop", "ro.build.version.sdk")
        exec(adbPath.toString(), "shell", "getprop", "ro.build.version.release")
        exec(adbPath.toString(), "shell", "getprop", "ro.product.model")
        exec(adbPath.toString(), "shell", "getprop", "ro.product.brand")
        exec(adbPath.toString(), "shell", "getprop", "ro.product.name")
        exec(adbPath.toString(), "shell", "getprop", "ro.product.board")
        exec(adbPath.toString(), "shell", "getprop", "ro.product.cpu.abilist")
        exec(adbPath.toString(), "shell", "getprop", "ro.sf.Idc_density")
    }
}