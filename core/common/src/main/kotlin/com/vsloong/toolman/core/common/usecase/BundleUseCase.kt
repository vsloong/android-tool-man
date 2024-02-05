package com.vsloong.toolman.core.common.usecase


import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.common.model.KeystoreModel
import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase
import java.lang.StringBuilder
import java.nio.file.Path

class BundleUseCase(
    private val assetsManager: IAssetsPath
) : ICmdUseCase {

    fun logVersion() {
        run(cmd = "${cmdName()} version")
    }

    /**
     * 注意：命令的最后不能有空格
     */
    fun buildApks(
        aabPath: Path,
        outputApksPath: Path,
        universal: Boolean = true,
        keyStoreModel: KeystoreModel? = null
    ) {

        // 存放签名文件的文件夹
        val keystoreDirPath = WorkspaceManager.getLocalKeystoreDirPath()

        val cmd = StringBuilder("${cmdName()} build-apks --bundle=$aabPath --output=${outputApksPath} ")

        keyStoreModel?.let {
            cmd.append("--ks=${keystoreDirPath.resolve(it.keystoreFileName)} ")
            cmd.append("--ks-pass=pass:${it.keystorePass} ")
            cmd.append("--ks-key-alias=${it.keyAlias} ")
            cmd.append("--key-pass=pass:${it.keyPass} ")
        }

        // 包含所有代码和资源（功能模块）
        if (universal) {
            cmd.append("--mode=universal ")
        }

        // 覆盖之前生成的包
        cmd.append("--overwrite")

        run(cmd = cmd.toString().trim())
    }

    fun installApks(apksPath: Path, deviceId: String) {
        installApks(apksPath = apksPath, devices = setOf(deviceId))
    }

    fun installApks(apksPath: Path, devices: Set<String> = emptySet()) {
        devices.forEach { device ->
            run(cmd = "${cmdName()} install-apks --apks=$apksPath --device-id=$device")
        }
    }

    override fun cmdName(): String {
        return CmdType.BundleTool.cmdName
    }

    override fun cmdPath(): String {
        return "java -jar ${assetsManager.getBundleToolJarPath()}"
    }
}