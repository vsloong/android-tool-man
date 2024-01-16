package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase
import java.nio.file.Path

class ApkToolUseCase(
    private val iAssetsPath: IAssetsPath
) : ICmdUseCase {

    fun decodeApk(apkPath: Path, outputDir: Path) {
        run(cmd = "apktool decode $apkPath -o $outputDir")
    }

    fun buildApk(dir: Path, outputApkPath: Path) {
        run(cmd = "${cmdName()} build $dir -o $outputApkPath")
    }

    override fun cmdName(): String {
        return CmdType.ApkTool.cmdName
    }

    override fun cmdPath(): String {
        return "java -jar ${iAssetsPath.getApkToolJarPath()}"
    }
}