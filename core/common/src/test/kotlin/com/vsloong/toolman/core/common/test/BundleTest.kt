package com.vsloong.toolman.core.common.test

import com.vsloong.toolman.core.common.usecase.BundleUseCase
import com.vsloong.toolman.core.common.utils.logger
import com.vsloong.toolman.core.common.utils.unzip
import kotlin.io.path.Path

fun main() {


    val bundleUseCase = BundleUseCase(assetsManager = TestAssetsManager)


    val aabPath = Path("/Users/dragon/Temp/Bundle/sample.aab")

    val outputApksPath = aabPath.parent.resolve("sample.apks")

//    bundleUseCase.buildApks(
//        aabPath = aabPath,
//        outputApksPath = outputApksPath,
//    )
//    logger("apks文件构建完毕 ：$outputApksPath")
//
//
//    unzip(
//        zipFilePath = outputApksPath,
//    )
//    logger("apks文件解压完毕")

    bundleUseCase.installApks(apksPath = outputApksPath )
}