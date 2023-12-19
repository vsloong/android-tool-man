package com.vsloong.toolman.core.common.test

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.utils.isLinux
import com.vsloong.toolman.core.common.utils.isMacOs
import com.vsloong.toolman.core.common.utils.isWindows
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.toPath


/**
 * 资源（资产）相关用例
 */
object TestAssetsManager : IAssetsPath {


    /**
     * 测试使用工程实际地址的资产
     */
    override
    fun getAssetsPath(): Path {
        val projectPath = Path("/Users/dragon/Workspace/Desktop/android-tool-man")

        return projectPath
            .resolve("assets")
    }

    override fun getBundleToolJarPath(): Path {
        return getAssetsPath()
            .resolve("common")
            .resolve(bundleJarFileName)
    }

    override fun getAdbPath(): Path {
        val platform = if (isMacOs()) {
            "macos"
        } else if (isLinux()) {
            "linux"
        } else if (isWindows()) {
            "windows"
        } else {
            throw Throwable("not supported platform")
        }
        return getAssetsPath()
            .resolve(
                platform
            )
            .resolve("adb")
    }
}