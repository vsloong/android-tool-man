package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase
import java.nio.file.Path

/**
 *
 * apksigner参考内容
 * https://developer.android.google.cn/tools/apksigner?hl=zh-cn
 */
class ApkSignerUseCase(
    private val assetsManager: IAssetsPath,
) : ICmdUseCase {

    fun version(): String {
        return run(cmd = "${cmdName()} version").output
    }

    /**
     * 验证签名
     */
    fun verify(apkFile: Path) {
        run(cmd = "${cmdName()} verify --print-certs $apkFile")
    }

    override fun cmdName(): String {
        return CmdConstant.ApkSigner.cmdName
    }

    override fun cmdPath(): String {
        return "java -jar ${assetsManager.getApkSignerJarPath()}"
    }

}