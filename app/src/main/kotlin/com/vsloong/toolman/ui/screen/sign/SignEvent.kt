package com.vsloong.toolman.ui.screen.sign

import com.vsloong.toolman.core.common.model.KeystoreModel
import java.nio.file.Path

data class SignEvent(
    val onApkFileSelect: (Path) -> Unit,
    val onSignClick: () -> Unit,
    val onResignClick: () -> Unit,
    val onSelectKeystoreModel: (KeystoreModel) -> Unit,
    val onSaveKeystoreInfo: (Path, String, String, String) -> Unit,
    val onShowQrCode: (Path) -> Unit,
)
