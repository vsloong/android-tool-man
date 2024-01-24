package com.vsloong.toolman.core.common.manager

import java.nio.file.Files
import java.nio.file.Path
import javax.swing.filechooser.FileSystemView
import kotlin.io.path.Path


/**
 * 工作区配置
 */
object WorkspaceManager {

    private const val toolsName = "AndroidToolMan"

    // 远程工作区地址（手机端的地址）
    private val phoneWorkspacePath: Path = Path("/sdcard/$toolsName")
    val phoneCachePath: Path = phoneWorkspacePath.resolve("cache")


    // 本地工作区地址（电脑端的地址）
    private val localWorkspacePath: Path =
        FileSystemView.getFileSystemView().defaultDirectory.resolve(toolsName).toPath()

    /**
     * 本地缓存目录
     */
    fun getLocalCacheDirPath(): Path {
        val path = localWorkspacePath.resolve("cache")
        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }

        return path
    }

    /**
     * 本地缓存目录
     */
    fun getLocalServerDirPath(): Path {
        val path = localWorkspacePath.resolve("server")
        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }

        return path
    }

    /**
     * 本地存储的签名文件信息的文件夹
     */
    fun getLocalKeystoreDirPath(): Path {
        val path = localWorkspacePath.resolve("keystore")

        if (!Files.exists(path)) {
            Files.createDirectories(path)
        }

        return path
    }
}