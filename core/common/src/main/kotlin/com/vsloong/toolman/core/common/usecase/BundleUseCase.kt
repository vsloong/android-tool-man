package com.vsloong.toolman.core.common.usecase


import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.model.KeyStoreModel
import com.vsloong.toolman.core.common.utils.exec
import com.vsloong.toolman.core.common.utils.logger
import java.nio.file.Path

class BundleUseCase(
    private val assetsManager: IAssetsPath
) {

    fun logVersion() {
        exec(
            "java",
            "-jar",
            assetsManager.getBundleToolJarPath().toString(),
            "version",
            onLine = {

            }
        )
    }

    fun buildApks(
        aabPath: Path,
        universal: Boolean = true,
        keyStoreModel: KeyStoreModel? = null
    ): Path {

        val outputPath = aabPath.parent.resolve("output.apks")

        val cmdList = mutableListOf(
            "java",
            "-jar",
            assetsManager.getBundleToolJarPath().toString(),
            "build-apks",
            "--overwrite",  // 覆盖已有的文件
            "--bundle=$aabPath",
            "--output=${outputPath}",
        )

        // 包含所有代码和资源（功能模块）
        if (universal) {
            cmdList.add("--mode=universal")
        }

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
            onLine = {
                logger("执行中输出：$it")
            },
            onComplete = {
                logger("执行结束:$it")
            }
        )

        return outputPath
    }

    fun installApks(apksPath: Path, devices: Set<String>) {
        devices.forEach { device ->
            exec(
                "java",
                "-jar",
                assetsManager.getBundleToolJarPath().toString(),
                "install-apks",
                "--apks=${apksPath}",
                "--device-id=${device}",
                onLine = {
                    logger("安装输出：$it")
                },
                onComplete = {
                    logger("执行结束：$it")
                }
            )
        }
    }

}