package com.vsloong.toolman.core.common.manager

import java.nio.file.Files
import java.nio.file.Path

/**
 * Assets接口，提供资产目录
 */
interface IAssetsPath {

    val assetsPropertyKey: String get() = "compose.application.resources.dir"
    val bundleJarFileName: String get() = "bundletool-all-1.15.6.jar"
    val vasDollyJarFileName: String get() = "vas_dolly_3.0.6.jar"
    val walleJarFileName: String get() = "walle_1.1.6.jar"
    val apkSignerJarFileName: String get() = "apksigner.jar"
    val apkToolJarFileName: String get() = "apktool_2.9.2.jar"

    fun getAssetsPath(): Path

    fun getBundleToolJarPath(): Path {
        return getAssetsPath()
            .resolve(bundleJarFileName)
    }

    fun getVasDollyJarPath(): Path {
        return getAssetsPath()
            .resolve(vasDollyJarFileName)
    }

    fun getWalleJarPath(): Path {
        return getAssetsPath()
            .resolve(walleJarFileName)
    }

    fun getAdbPath(): Path {
        val path = getAssetsPath().resolve("adb")

        if (Files.exists(path)) {
            val file = path.toFile()
            if (!file.canExecute()) {
                file.setExecutable(true)
            }
        }
        return path
    }

    fun getApkSignerJarPath(): Path {
        return getAssetsPath().resolve(apkSignerJarFileName)
    }

    fun getApkToolJarPath(): Path {
        return getAssetsPath().resolve(apkToolJarFileName)
    }
}