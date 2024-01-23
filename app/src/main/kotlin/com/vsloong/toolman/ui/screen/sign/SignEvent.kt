package com.vsloong.toolman.ui.screen.sign

import java.nio.file.Path

data class SignEvent(
    val onKeyStoreFileSelect: (Path) -> Unit,
    val onApkFileSelect: (Path) -> Unit,
    val onSignClick: () -> Unit,
)
