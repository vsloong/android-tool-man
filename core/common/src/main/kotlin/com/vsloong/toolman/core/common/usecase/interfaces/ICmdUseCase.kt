package com.vsloong.toolman.core.common.usecase.interfaces

import com.vsloong.toolman.core.common.model.CmdOutput
import com.vsloong.toolman.core.common.utils.exec
import java.lang.StringBuilder

interface ICmdUseCase {

    fun cmdName(): String

    fun cmdPath(): String

    /**
     * 执行cmd命令获取结果
     */
    fun run(cmd: String, onLine: (String) -> Unit = {}): CmdOutput {
        val output = StringBuilder()
        val realCmd = cmd.replaceFirst(
            cmdName(),
            cmdPath()
        )
        exec(
            cmd = realCmd,
            onLine = {
                onLine.invoke(it)
                output.append(it).append("\n")
            })

        return CmdOutput(
            cmd = cmd,
            output = output.toString().trim()
        )
    }
}