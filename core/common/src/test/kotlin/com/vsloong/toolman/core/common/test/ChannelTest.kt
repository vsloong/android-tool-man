package com.vsloong.toolman.core.common.test

import com.vsloong.toolman.core.common.usecase.interfaces.IChannelUseCase
import com.vsloong.toolman.core.common.usecase.VasDollyUseCase
import com.vsloong.toolman.core.common.usecase.WalleUseCase
import com.vsloong.toolman.core.common.utils.logger
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

fun main() {

    val channelUseCase: IChannelUseCase = VasDollyUseCase(assetsManager = TestAssetsManager)
//    val channelUseCase: IChannelUseCase = WalleUseCase(assetsManager = TestAssetsManager)

//    val apkDir = Path("E:\\AndroidToolMan\\apk\\sample.apk")
    val apkPath = Path("/Users/dragon/Temp/HC/app-lite-qa-debug.apk")
    val srcApkName = apkPath.nameWithoutExtension


    // 添加渠道信息
    val channel = "xiaomi"
    channelUseCase.putChannel(
        apkPath = apkPath,
        channels = setOf(channel)
    )

    // 获取渠道信息
    val channelApkPath = (apkPath.parent).resolve("${srcApkName}_${channel}.apk")
    val channelInfo = channelUseCase.getChannel(
        apkPath = channelApkPath
    )

    logger("获取到的渠道信息是：$channelInfo")

    // 删除渠道信息
    channelUseCase.removeChannel(
        apkPath = channelApkPath
    )
}