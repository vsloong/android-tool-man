package com.vsloong.toolman.ui.screen.sign

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.manager.WorkspaceManager
import com.vsloong.toolman.core.common.model.KeystoreModel
import com.vsloong.toolman.core.common.model.SignInfo
import com.vsloong.toolman.core.common.usecase.ApkSignerUseCase
import com.vsloong.toolman.core.common.usecase.ZipAlignUseCase
import com.vsloong.toolman.core.common.utils.getFileMD5
import com.vsloong.toolman.manager.AssetsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

class SignViewModel(
    private val zipAlignUseCase: ZipAlignUseCase = ZipAlignUseCase(iAssetsPath = AssetsManager),
    private val apkSignerUseCase: ApkSignerUseCase = ApkSignerUseCase(assetsManager = AssetsManager)
) : BaseViewModel() {

    val signEvent = SignEvent(
        onKeyStoreFileSelect = {
            keystoreFile.value = it
        },
        onApkFileSelect = {
            apkFile.value = it
            check()
        },
        onSignClick = {
            sign()
        },
        onResignClick = {
            signState.value = SignState.NeedSign
        },
        onSelectKeystoreModel = {
            if (selectKeystoreModel.value == it) {
                selectKeystoreModel.value = KeystoreModel()
            } else {
                selectKeystoreModel.value = it
            }
        }
    )

    val apkFile = mutableStateOf(Path(""))
    val keystoreFile = mutableStateOf(Path(""))
    val keystorePass = mutableStateOf("")
    val keyAlias = mutableStateOf("")
    val keyPass = mutableStateOf("")


    // 已签名的APK信息
    val signInfo = mutableStateOf(SignInfo())
    val signState = mutableStateOf<SignState>(SignState.Idle)

    // 选中的已有的签名信息
    val keystoreInfo = mutableStateListOf<KeystoreModel>()
    val selectKeystoreModel = mutableStateOf(KeystoreModel())

    init {
        keystoreInfo.clear()
        keystoreInfo.addAll(getKeystoreInfo())
    }

    /**
     * 检测签名
     */
    private fun check() {
        viewModelScope.launch(Dispatchers.IO) {

            signState.value = SignState.Checking

            val result = apkSignerUseCase.verify(apkFile = apkFile.value)

            if (result == null) {
                signState.value = SignState.UnSign
            } else {
                signState.value = SignState.Signed
                signInfo.value = result
            }
        }
    }

    /**
     * 签名
     */
    private fun sign() {
        viewModelScope.launch(Dispatchers.IO) {

            val sourceApkFile = apkFile.value

            // 先对齐
            val alignedApkFile = sourceApkFile.parent.resolve("${sourceApkFile.nameWithoutExtension}_aligned.apk")
            zipAlignUseCase.align(
                apkFile = sourceApkFile,
                outputApkFile = alignedApkFile
            )


            // 选择了签名信息则使用选择的签名
            if (selectKeystoreModel.value.hasData()) {
                // 直接使用选择的签名信息进行签名
                val signedApkFile = alignedApkFile.parent.resolve("${alignedApkFile.nameWithoutExtension}_signed.apk")
                apkSignerUseCase.sign(
                    apkFile = alignedApkFile,
                    keystoreFile = selectKeystoreModel.value.getKeystoreFilePath(),
                    keystorePass = selectKeystoreModel.value.keystorePass,
                    keyAlias = selectKeystoreModel.value.keyAlias,
                    keyPass = selectKeystoreModel.value.keyPass,
                    outputApkFile = signedApkFile
                )
            } else {
                // 先签名
                val signedApkFile = alignedApkFile.parent.resolve("${alignedApkFile.nameWithoutExtension}_signed.apk")
                apkSignerUseCase.sign(
                    apkFile = alignedApkFile,
                    keystoreFile = keystoreFile.value,
                    keystorePass = keystorePass.value,
                    keyAlias = keyAlias.value,
                    keyPass = keyPass.value,
                    outputApkFile = signedApkFile
                )

                // 存储签名信息
                saveKeystoreInfo(
                    keystoreFilePath = keystoreFile.value,
                    keystorePass = keystorePass.value,
                    keyAlias = keyAlias.value,
                    keyPass = keyPass.value,
                )
            }

        }
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

    /**
     * 存储签名信息
     */
    private fun saveKeystoreInfo(
        keystoreFilePath: Path,
        keystorePass: String,
        keyAlias: String,
        keyPass: String,
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
            keystoreFileMd5 = getFileMD5(keystoreFile.value)
        )

        val keystoreInfo = getKeystoreInfo().toMutableList()
        keystoreInfo.removeIf {
            it.keystoreFileMd5 == keystoreModel.keystoreFileMd5
        }
        keystoreInfo.add(keystoreModel)

        ObjectMapper().writeValue(getKeystoreInfoFile(), keystoreInfo)
    }
}