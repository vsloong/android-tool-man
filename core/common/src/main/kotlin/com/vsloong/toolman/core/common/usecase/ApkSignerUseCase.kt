package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.utils.exec
import java.nio.file.Path
import kotlin.io.path.absolutePathString

/**
 *
 * apksigner参考内容
 * https://developer.android.google.cn/tools/apksigner?hl=zh-cn
 */
class ApkSignerUseCase(
    assetsManager: IAssetsPath,
) {

    private val apkSignerPath = assetsManager.getApkSignerJarPath().absolutePathString()

    fun version(): String {
        var version = ""
        exec(
            cmd = "java -jar $apkSignerPath version",
            onLine = {
                version = it
            })
        return version
    }

    /**
     * 验证签名
     */
    fun verify(apkFile: Path) {
        exec(
            cmd = "java -jar $apkSignerPath verify --print-certs $apkFile"
        )
    }
}