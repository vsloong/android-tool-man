package com.vsloong.toolman.ui.screen.sign

import java.nio.file.Path

data class KeystoreEvent(
    val onSaveKeystoreInfo: (Path, String, String, String) -> Unit,
)
