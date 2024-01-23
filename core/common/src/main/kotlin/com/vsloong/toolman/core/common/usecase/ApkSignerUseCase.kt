package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.model.SignInfo
import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

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
    fun verify(apkFile: Path): SignInfo? {
        var md5 = ""
        var sha1 = ""
        var sha256 = ""

        run(
            cmd = "${cmdName()} verify --print-certs $apkFile",
            onLine = {
                if (it.startsWith("Signer #1 certificate SHA-256 digest:")) {
                    sha256 = it.split(":").last().trim()
                } else if (it.startsWith("Signer #1 certificate SHA-1 digest:")) {
                    sha1 = it.split(":").last().trim()
                } else if (it.startsWith("Signer #1 certificate MD5 digest:")) {
                    md5 = it.split(":").last().trim()
                }
            })

        if (md5.isNotBlank() && sha1.isNotBlank() && sha256.isNotBlank()) {
            return SignInfo(
                md5 = md5,
                sha1 = sha1,
                sha256 = sha256
            )
        }

        return null
    }

    /**
     * 签名
     */
    fun sign(
        apkFile: Path,
        keystoreFile: Path,
        keystorePass: String,
        keyAlias: String,
        keyPass: String,
        outputApkFile: Path
    ) {
        run(cmd = "${cmdName()} sign --verbose --ks $keystoreFile --ks-pass pass:$keystorePass --ks-key-alias $keyAlias --key-pass pass:$keyPass --out $outputApkFile $apkFile")
    }

    override fun cmdName(): String {
        return CmdType.ApkSigner.cmdName
    }

    override fun cmdPath(): String {
        return "java -jar ${assetsManager.getApkSignerJarPath()}"
    }

}