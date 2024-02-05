package com.vsloong.toolman.ui.screen.device

import java.nio.file.Path

data class DeviceEvent(
    val onExecuteClick: (String) -> Unit,
    val onInstall: (List<Path>) -> Unit,
)