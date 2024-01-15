package com.vsloong.toolman.core.common.usecase

sealed class CmdType(val cmdName: String) {
    object Adb : CmdType(cmdName = "adb")
    object ApkSigner : CmdType(cmdName = "apksigner")
    object VasDolly : CmdType(cmdName = "vasdolly")
    object Walle : CmdType(cmdName = "walle")
    object BundleTool : CmdType(cmdName = "bundletool")

}