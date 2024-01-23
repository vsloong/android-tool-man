package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase
import java.nio.file.Path

class ZipAlignUseCase(
    private val iAssetsPath: IAssetsPath
) : ICmdUseCase {

    /**
     * 检查是否对齐
     * -v 详细输出
     * 如果已对齐，最后会输出 “Verification succesful”
     */
    fun check(apkFile: Path): Boolean {
        var success = false
        run(
            cmd = "${cmdName()} -c -v 4 $apkFile",
            onLine = {
                success = it.startsWith("Verification succesful")
            })
        return success
    }

    /**
     * 对齐文件
     * -v 详细输出
     */
    fun align(apkFile: Path, outputApkFile: Path) {
        run(cmd = "${cmdName()} -p -f -v 4 $apkFile $outputApkFile")
    }

    override fun cmdName(): String {
        return CmdType.ZipAlign.cmdName
    }

    override fun cmdPath(): String {
        return iAssetsPath.getZipAlignPath().toString()
    }
}