package com.vsloong.toolman.manager

import java.nio.file.Files
import java.nio.file.Path

/**
 * Assets接口，提供资产目录
 */
interface IAssetsPath {

    val assetsPropertyKey: String get() = "compose.application.resources.dir"

    fun getAssetsPath(): Path

    fun getBundleToolJarPath(): Path {
        return getAssetsPath()
            .resolve("bundletool-all-1.15.6.jar")
    }

    fun getAdbPath(): Path {
        val path = AssetsManager.getAssetsPath().resolve("adb")

        if (Files.exists(path)) {
            val file = path.toFile()
            if (!file.canExecute()) {
                file.setExecutable(true)
            }
        }
        return path
    }
}