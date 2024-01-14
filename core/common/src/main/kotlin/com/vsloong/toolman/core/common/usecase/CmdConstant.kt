package com.vsloong.toolman.core.common.usecase

sealed class CmdConstant(val cmdName: String) {
    object Adb : CmdConstant(cmdName = "adb")
    object ApkSigner : CmdConstant(cmdName = "apksigner")
    object VasDolly : CmdConstant(cmdName = "vasdolly")
    object Walle : CmdConstant(cmdName = "walle")

}