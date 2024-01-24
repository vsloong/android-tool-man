package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase
import java.nio.file.Path

class KeyToolUseCase : ICmdUseCase {

    /**
     * 验证秘钥库密码是否正确
     */
    fun verifyKeystorePass(
        keystorePath: Path,
        keystorePass: String
    ): Boolean {
        var passWrong = false
        run(cmd = "${cmdName()} -list -v -keystore $keystorePath -storepass $keystorePass",
            onLine = {
                if (it.contains("keystore password was incorrect")) {
                    passWrong = true
                }
            }
        )

        return !passWrong
    }

    override fun cmdName(): String {
        return CmdType.KeyTool.cmdName
    }

    override fun cmdPath(): String {
        return cmdName()
    }
}