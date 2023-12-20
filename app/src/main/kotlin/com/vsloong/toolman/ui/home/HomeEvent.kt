package com.vsloong.toolman.ui.home

import com.vsloong.toolman.core.common.model.AdbDeviceInfo

data class HomeEvent(
    val onDeviceSelect: (AdbDeviceInfo) -> Unit,
    val onDeviceRemove: (AdbDeviceInfo) -> Unit,
    val onClearAllDevice: () -> Unit
)
