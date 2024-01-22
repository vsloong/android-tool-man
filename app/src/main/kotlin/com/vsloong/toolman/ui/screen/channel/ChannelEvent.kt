package com.vsloong.toolman.ui.screen.channel

import java.nio.file.Path

data class ChannelEvent(
    val onSelectApk: (List<Path>) -> Unit,
    val onSelectChannelTool: (String) -> Unit,
    val onPutChannel: (String) -> Unit,
    val onGetChannel: () -> Unit,
    val onRemoveChanel: () -> Unit,
)