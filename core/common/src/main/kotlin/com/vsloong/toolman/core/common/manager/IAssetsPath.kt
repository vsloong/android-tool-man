package com.vsloong.toolman.core.common.manager

import java.nio.file.Files
import java.nio.file.Path

/**
 * Assets接口，提供资产目录
 */
interface IAssetsPath {

    val assetsPropertyKey: String get() = "compose.application.resources.dir"
    val bundleJarFileName: String get() = "bundletool-all-1.15.6.jar"

    fun getAssetsPath(): Path

    fun getBundleToolJarPath(): Path {
        return getAssetsPath()
            .resolve(bundleJarFileName)
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
}