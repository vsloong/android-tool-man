package com.vsloong.toolman.core.common.model

sealed class KeystoreError(val errorMessage: String) {
    object KeystorePassError : KeystoreError(errorMessage = "keystore pass error")

    object KeyAliasError : KeystoreError(errorMessage = "key alias error")

    object KeyPassError : KeystoreError(errorMessage = "key pass error")

    object None : KeystoreError(errorMessage = "")
}