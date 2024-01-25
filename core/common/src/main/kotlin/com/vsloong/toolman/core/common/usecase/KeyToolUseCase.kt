package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.model.KeystoreError
import com.vsloong.toolman.core.common.model.KeystoreVerify
import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase
import java.nio.file.Path

class KeyToolUseCase : ICmdUseCase {

    /**
     * 验证秘钥库密码是否正确
     */
    fun verifyKeystore(
        keystorePath: Path,
        keystorePass: String,
        keyAlias: String,
        keyPass: String
    ): KeystoreVerify {
        var keystoreError: KeystoreError = KeystoreError.None

        var sha1 = ""
        var sha256 = ""

        run(cmd = "${cmdName()} -list -v -keystore $keystorePath -storepass $keystorePass -alias $keyAlias -keypass $keyPass",
            onLine = {

                // 表明keyPass错误(只有新版 PKCS12 格式的支持，旧版不支持)
                if (it.contains("-keypass")) {
                    keystoreError = KeystoreError.KeyPassError
                }

                // 表明keyAlias错误（只有新版 PKCS12 格式的支持，旧版不支持）
                if (it.contains("<$keyAlias>")) {
                    keystoreError = KeystoreError.KeyAliasError
                }

                if (it.contains("SHA1:")) {
                    sha1 = it.replace("SHA1:", "").trim()
                } else if (it.contains("SHA256:")) {
                    sha256 = it.replace("SHA256:", "").trim()
                }
            }
        )

        if (keystoreError is KeystoreError.None) {
            if (sha1.isEmpty() || sha256.isEmpty()) {
                keystoreError = KeystoreError.KeystorePassError
            }
        }

        return KeystoreVerify(
            keystoreError = keystoreError,
            sha1 = sha1,
            sha256 = sha256
        )
    }

    override fun cmdName(): String {
        return CmdType.KeyTool.cmdName
    }

    override fun cmdPath(): String {
        return cmdName()
    }
}