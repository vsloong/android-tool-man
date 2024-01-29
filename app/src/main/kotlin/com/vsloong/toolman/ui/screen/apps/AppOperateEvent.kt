package com.vsloong.toolman.ui.screen.apps

data class AppOperateEvent(
    val onClearData: (String) -> Unit,
    val onUnInstall: (String) -> Unit,
    val onPullApk: (String) -> Unit,
)
