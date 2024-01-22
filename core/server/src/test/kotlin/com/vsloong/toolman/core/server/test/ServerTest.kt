package com.vsloong.toolman.core.server.test

import com.vsloong.toolman.core.server.ServerUseCase
import com.vsloong.toolman.core.server.utils.getIpAddress
import kotlin.io.path.Path

fun main() {
    val serverUseCase = ServerUseCase()

    println("IP地址：${getIpAddress()}")

//    serverUseCase.start(port = 8899, localDirPath = Path("/Users/dragon/AndroidToolMan"))
}