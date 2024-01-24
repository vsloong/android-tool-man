package com.vsloong.toolman.core.server

import java.nio.file.Path

data class ServerConfig(
    val port: Int,
    val localServerDirPath: Path
)