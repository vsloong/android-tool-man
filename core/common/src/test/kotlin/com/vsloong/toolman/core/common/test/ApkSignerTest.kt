package com.vsloong.toolman.core.common.test

import com.vsloong.toolman.core.common.usecase.ApkSignerUseCase
import com.vsloong.toolman.core.common.utils.logger
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.name

fun main() {

    val apkSignerUseCase = ApkSignerUseCase(TestAssetsManager)
    val version = apkSignerUseCase.version()
    logger("apksigner version = $version")

//    apkSignerUseCase.verify(Path("/Users/dragon/Temp/HC/app-lite-qa-debug.apk"))

}