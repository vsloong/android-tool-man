package com.vsloong.toolman.core.common.test

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.utils.isLinux
import com.vsloong.toolman.core.common.utils.isMacOs
import com.vsloong.toolman.core.common.utils.isWindows
import test_local_ignore.PROJECT_PATH
import java.nio.file.Path
import kotlin.io.path.Path


/**
 * 资源（资产）相关用例
 */
object TestAssetsManager : IAssetsPath {


    /**
     * 测试使用工程实际地址的资产
     */
    override
    fun getAssetsPath(): Path {

        // 当前工程的本机地址
        val projectPath = Path(PROJECT_PATH)

        return projectPath
            .resolve("assets")
    }

    override fun getBundleToolJarPath(): Path {
        return getAssetsPath()
            .resolve("common")
            .resolve(bundleJarFileName)
    }

    override fun getVasDollyJarPath(): Path {
        return getAssetsPath()
            .resolve("common")
            .resolve(vasDollyJarFileName)
    }

    override fun getWalleJarPath(): Path {
        return getAssetsPath()
            .resolve("common")
            .resolve(walleJarFileName)
    }

    override fun getAdbPath(): Path {
        return getAssetsPath()
            .resolve(
                getPlatForm()
            )
            .resolve("adb")
    }

    override fun getZipAlignPath(): Path {
        return getAssetsPath()
            .resolve(
                getPlatForm()
            )
            .resolve("zipalign")
    }

    override fun getApkSignerJarPath(): Path {
        return getAssetsPath()
            .resolve("common")
            .resolve(apkSignerJarFileName)
    }

    override fun getApkToolJarPath(): Path {
        return getAssetsPath()
            .resolve("common")
            .resolve(apkToolJarFileName)
    }

    private fun getPlatForm(): String {
        return if (isMacOs()) {
            "macos"
        } else if (isLinux()) {
            "linux"
        } else if (isWindows()) {
            "windows"
        } else {
            throw Throwable("not supported platform")
        }
    }
}