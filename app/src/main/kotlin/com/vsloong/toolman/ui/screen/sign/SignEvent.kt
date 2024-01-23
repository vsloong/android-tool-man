package com.vsloong.toolman.ui.screen.sign

import com.vsloong.toolman.core.common.model.KeystoreModel
import java.nio.file.Path

data class SignEvent(
    val onKeyStoreFileSelect: (Path) -> Unit,
    val onApkFileSelect: (Path) -> Unit,
    val onSignClick: () -> Unit,
    val onResignClick: () -> Unit,
    val onSelectKeystoreModel: (KeystoreModel) -> Unit
)
