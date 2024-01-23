package com.vsloong.toolman.core.common.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import java.nio.file.Path

/**
 * 签名及秘钥信息
 */
class KeystoreModel(

    // 签名文件的文件名（只是存放了文件名，文件的路径需要根据本地路劲获取，方便迁移）
    val keystoreFileName: String = "",
    val keystorePass: String = "",
    val keyAlias: String = "",
    val keyPass: String = "",

    // 签名文件md5
    val keystoreFileMd5: String = "",
) {

    fun hasData(): Boolean {
        return keystoreFileName.isNotEmpty()
                && keystoreFileMd5.isNotEmpty()
                && keystorePass.isNotEmpty()
                && keyAlias.isNotEmpty()
                && keyPass.isNotEmpty()
    }

    @JsonIgnore
    fun getKeystoreFilePath(): Path {
        return WorkspaceManager.getLocalKeystoreDirPath().resolve(keystoreFileName)
    }
}
