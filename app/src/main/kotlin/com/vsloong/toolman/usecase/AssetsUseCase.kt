package com.vsloong.toolman.usecase

import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path


/**
 * 资源（资产）相关用例
 */
class AssetsUseCase {

    /**
     * 获取Compose桌面端应用的资产文件路径（注意区分资源文件）
     *
     * 该属性需要一定的条件，必须是执行过prepareAppResources的task后才会有，否则为null
     * debug环境下，需要执行gradle的run任务
     *
     * 在Main文件中直接运行main()函数则获取不到
     */
    fun getAssetsPath(): Path {
        return Path(System.getProperty("compose.application.resources.dir"))
    }

    fun getBundleToolJarPath(): Path {
        return getAssetsPath()
            .resolve("bundletool-all-1.15.6.jar")
    }

    fun getAdbPath(): Path {

//        val fileName = when (hostOs) {
//            OS.Windows -> {
//                "adb.exe"
//            }
//
//            else -> {
//                "adb"
//            }
//        }

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