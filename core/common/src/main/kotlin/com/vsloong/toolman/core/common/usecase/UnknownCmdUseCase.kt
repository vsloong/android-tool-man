package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.model.CmdOutput
import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase

class UnknownCmdUseCase(val cmdName: String) : ICmdUseCase {
    override fun cmdName(): String {
        return cmdName
    }

    override fun cmdPath(): String {
        return "null"
    }

    override fun run(cmd: String, onLine: (String) -> Unit): CmdOutput {
        return CmdOutput(
            cmd = cmd,
            output = "unknown command [$cmdName]"
        )
    }
}