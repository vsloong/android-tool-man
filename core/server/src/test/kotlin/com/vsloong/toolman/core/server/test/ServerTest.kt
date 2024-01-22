package com.vsloong.toolman.core.server.test

import com.vsloong.toolman.core.server.ServerUseCase
import kotlin.io.path.Path

fun main() {
    val serverUseCase = ServerUseCase()
    serverUseCase.start(port = 8899, localDirPath = Path("/Users/dragon/AndroidToolMan"))
}