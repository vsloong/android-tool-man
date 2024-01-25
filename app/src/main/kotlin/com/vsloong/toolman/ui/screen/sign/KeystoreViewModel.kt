package com.vsloong.toolman.ui.screen.sign

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.common.model.KeystoreError
import com.vsloong.toolman.core.common.model.KeystoreModel
import com.vsloong.toolman.core.common.usecase.KeyToolUseCase
import com.vsloong.toolman.core.common.utils.getFileMD5
import com.vsloong.toolman.core.common.utils.logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Path
import kotlin.io.path.copyTo
import kotlin.io.path.name
import kotlin.math.log

class KeystoreViewModel(
    private val keyToolUseCase: KeyToolUseCase = KeyToolUseCase()
) : BaseViewModel() {

    val toastEvent = MutableSharedFlow<String>()

    /**
     * 是否展示配置弹窗
     */
    val showKeystoreConfigDialog = mutableStateOf(false)

    val keystoreEvent = KeystoreEvent(
        onSaveKeystoreInfo = { keystoreFile, keystorePass, keyAlias, keyPass ->
            viewModelScope.launch(Dispatchers.IO) {
                checkKeystore(
                    keystoreFilePath = keystoreFile,
                    keystorePass = keystorePass,
                    keyAlias = keyAlias,
                    keyPass = keyPass
                )
            }
        }
    )

    // 已存储的签名数据
    val keystoreListInfo = mutableStateListOf<KeystoreModel>()

    init {
        keystoreListInfo.clear()
        keystoreListInfo.add(KeystoreModel())//一个空数据
        keystoreListInfo.addAll(getKeystoreInfo())
    }

    /**
     * 存储签名信息的文件
     */
    private fun getKeystoreInfoFile(): File {
        val localSignDirPath = WorkspaceManager.getLocalKeystoreDirPath()
        val keystoreInfoFile = localSignDirPath.resolve("keystoreInfo.json")
        return keystoreInfoFile.toFile()
    }

    /**
     * 获取签名信息
     */
    private fun getKeystoreInfo(): List<KeystoreModel> {
        val file = getKeystoreInfoFile()
        val objectMapper = ObjectMapper()
        return try {
            objectMapper.readValue(file, object : TypeReference<List<KeystoreModel>>() {})
        } catch (e: Throwable) {
            emptyList()
        }
    }

    private suspend fun checkKeystore(
        keystoreFilePath: Path,
        keystorePass: String,
        keyAlias: String,
        keyPass: String,
    ) {

        val result = keyToolUseCase.verifyKeystore(
            keystorePath = keystoreFilePath,
            keystorePass = keystorePass,
            keyAlias = keyAlias,
            keyPass = keyPass
        )

        // 密码都正确进行保存
        if (result.keystoreError is KeystoreError.None) {
            saveKeystoreInfo(
                keystoreFilePath = keystoreFilePath,
                keystorePass = keystorePass,
                keyAlias = keyAlias,
                keyPass = keyPass,
                sha1 = result.sha1,
                sha256 = result.sha256
            )

            showKeystoreConfigDialog.value = false

        } else {
            toastEvent.tryEmit(result.keystoreError.errorMessage)
            toastEvent.emit(result.keystoreError.errorMessage)
            println(result.keystoreError.errorMessage)
        }
    }

    /**
     * 存储签名信息
     */
    private fun saveKeystoreInfo(
        keystoreFilePath: Path,
        keystorePass: String,
        keyAlias: String,
        keyPass: String,
        sha1: String,
        sha256: String,
    ) {

        // 签名文件的名称
        val keystoreFileName = keystoreFilePath.name

        val newKeystoreFilePath = getKeystoreInfoFile().parentFile.toPath()
            .resolve(keystoreFileName)

        // 拷贝签名文件到本软件的相关数据文件夹
        keystoreFilePath.copyTo(newKeystoreFilePath, true)

        val keystoreModel = KeystoreModel(
            keystoreFileName = keystoreFileName,
            keystorePass = keystorePass,
            keyAlias = keyAlias,
            keyPass = keyPass,
            keystoreFileMd5 = getFileMD5(keystoreFilePath),
            sha1 = sha1,
            sha256 = sha256
        )

        val keystoreInfo = getKeystoreInfo().toMutableList()
        keystoreInfo.removeIf {
            it.keystoreFileMd5 == keystoreModel.keystoreFileMd5
        }
        keystoreInfo.add(keystoreModel)

        ObjectMapper().writeValue(getKeystoreInfoFile(), keystoreInfo)
    }
}