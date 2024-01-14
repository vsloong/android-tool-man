package com.vsloong.toolman.core.common.usecase.factory

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.model.CmdOutput
import com.vsloong.toolman.core.common.usecase.*
import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase
import com.vsloong.toolman.core.common.utils.whitespace

class UseCaseFactory(
    private val assetsManager: IAssetsPath,
) {

    private val useCaseMap: MutableMap<String, ICmdUseCase> = mutableMapOf()

    fun run(cmd: String): CmdOutput {
        return getUseCase(cmd).run(cmd)
    }

    private fun getUseCase(cmd: String): ICmdUseCase {
        val cmdName = cmd.split(whitespace).first()

        var useCase = useCaseMap[cmdName]

        if (useCase == null) {
            useCase = when (cmdName) {
                CmdConstant.Adb.cmdName -> {
                    AdbUseCase(assetsManager = assetsManager)
                }

                CmdConstant.ApkSigner.cmdName -> {
                    ApkSignerUseCase(assetsManager = assetsManager)
                }

                CmdConstant.VasDolly.cmdName -> {
                    VasDollyUseCase(assetsManager = assetsManager)
                }

                CmdConstant.Walle.cmdName -> {
                    WalleUseCase(assetsManager = assetsManager)
                }

                else -> {
                    UnknownCmdUseCase(cmdName = cmdName)
                }
            }

            useCaseMap[cmdName] = useCase
        }

        return useCase
    }
}