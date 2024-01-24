package com.vsloong.toolman.manager

import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.server.ServerConfig

object AppManager {

    fun getServerConfig(): ServerConfig {
        return ServerConfig(port = 9527, localServerDirPath = WorkspaceManager.getLocalServerDirPath())
    }
}