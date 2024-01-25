package com.vsloong.toolman.core.common.model

data class KeystoreVerify(
    val keystoreError: KeystoreError,
    val sha1: String,
    val sha256: String
)
