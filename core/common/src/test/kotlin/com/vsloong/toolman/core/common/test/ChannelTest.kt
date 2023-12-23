package com.vsloong.toolman.core.common.test

import com.vsloong.toolman.core.common.usecase.IChannelUseCase
import com.vsloong.toolman.core.common.usecase.VasDollylUseCase
import com.vsloong.toolman.core.common.usecase.WalleUseCase
import com.vsloong.toolman.core.common.utils.logger
import kotlin.io.path.Path

fun main() {

    val channelUseCase: IChannelUseCase = VasDollylUseCase(assetsManager = TestAssetsManager)
//    val channelUseCase: IChannelUseCase = WalleUseCase(assetsManager = TestAssetsManager)

    val apkDir = Path("E:\\AndroidToolMan\\apk\\")
    val apkPath = apkDir.resolve("sample.apk")

    // 添加渠道信息
    channelUseCase.putChannel(
        apkPath = apkPath,
        channels = setOf("xiaomi")
    )

    // 获取渠道信息
    val channelApkPath = apkDir.resolve("sample_xiaomi.apk")
    val channel = channelUseCase.getChannel(
        apkPath = channelApkPath
    )

    logger("获取到的渠道信息是：$channel")

    // 删除渠道信息
    channelUseCase.removeChannel(
        apkPath = channelApkPath
    )
}