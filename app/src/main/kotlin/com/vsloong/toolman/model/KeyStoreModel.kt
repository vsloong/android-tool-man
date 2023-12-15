package com.vsloong.toolman.model

import java.io.File

/**
 * 签名及秘钥信息
 */
data class KeyStoreModel(
    val keyStoreFile: File,
    val keystorePass: String,
    val keyAlias: String,
    val keyPass: String,
)
