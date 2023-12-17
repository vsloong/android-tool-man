package com.vsloong.toolman.usecase


import com.vsloong.toolman.manager.AssetsManager
import com.vsloong.toolman.model.KeyStoreModel
import com.vsloong.toolman.utils.exec
import com.vsloong.toolman.utils.logger
import java.nio.file.Path

class BundleUseCase(
    private val assetsManager: AssetsManager = AssetsManager
) {

    fun logVersion() {
        exec(
            "java",
            "-jar",
            assetsManager.getBundleToolJarPath().toString(),
            "version",
            onMessage = {
                logger("输出结果：$it")
            }
        )
    }

    fun buildApks(
        aabPath: Path,
        keyStoreModel: KeyStoreModel? = null
    ) {

        val cmdList = mutableListOf(
            "java",
            "-jar",
            assetsManager.getBundleToolJarPath().toString(),
            "build-apks",
            "--overwrite",  // 覆盖已有的文件
//                "--mode=universal",     // 包含所有代码和资源（功能模块）
//                "--connected-device",   // 针对已连接设备的配置构建 APK
            "--bundle=$aabPath",
            "--output=${aabPath.parent.resolve("output.apks")}",
        )

        keyStoreModel?.let {
            cmdList.apply {
                add("--ks=${it.keyStoreFile.absolutePath}")
                add("--ks-pass=pass:${it.keystorePass}")
                add("--ks-key-alias=${it.keyAlias}")
                add("--key-pass=pass:${it.keyPass}")
            }
        }


        exec(
            cmdList,
            onMessage = {
                logger("执行中输出：$it")
            },
            onComplete = {
                logger("执行结束:$it")
            }
        )

    }

    fun installApk(apksPath: Path) {

        exec(
            "java",
            "-jar",
            assetsManager.getBundleToolJarPath().toString(),
            "install-apks",
            "--apks=${apksPath}",
            onMessage = {
                logger("安装输出：$it")
            },
            onComplete = {
                logger("执行结束：$it")
            }
        )
    }

}