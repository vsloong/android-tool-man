package com.vsloong.toolman.test

import com.vsloong.toolman.usecase.BundleUseCase
import com.vsloong.toolman.utils.logger
import com.vsloong.toolman.utils.unzip
import kotlin.io.path.Path
import kotlin.io.path.toPath

fun main() {

    val bundleUseCase = BundleUseCase(assetsManager = TestAssetsManager)

    val apksPath = bundleUseCase.buildApks(
        aabPath = Path("E:\\Bundle\\sample.aab"),
        universal = false
    )

    logger("apks文件构建完毕 ：$apksPath")

    unzip(
        zipFilePath = apksPath,
    )

    logger("apks文件解压完毕")
}