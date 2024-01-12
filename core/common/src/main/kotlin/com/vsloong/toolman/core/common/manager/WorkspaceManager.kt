package com.vsloong.toolman.core.common.manager

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
    private val localWorkspacePath: Path = FileSystemView.getFileSystemView().homeDirectory.resolve(toolsName).toPath()
    val localCachePath: Path = localWorkspacePath.resolve("cache")
}