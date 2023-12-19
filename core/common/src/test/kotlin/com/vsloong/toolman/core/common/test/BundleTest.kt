package com.vsloong.toolman.core.common.test

import com.vsloong.toolman.core.common.usecase.BundleUseCase
import com.vsloong.toolman.core.common.utils.logger
import com.vsloong.toolman.core.common.utils.unzip
import kotlin.io.path.Path

fun main() {


    val bundleUseCase = BundleUseCase(assetsManager = TestAssetsManager)

    val apksPath = bundleUseCase.buildApks(
//        aabPath = Path("E:\\Bundle\\sample.aab"),
        aabPath = Path("/Users/dragon/Temp/Bundle/sample.aab"),
        universal = true
    )

    logger("apks文件构建完毕 ：$apksPath")

    unzip(
        zipFilePath = apksPath,
    )

    logger("apks文件解压完毕")
}