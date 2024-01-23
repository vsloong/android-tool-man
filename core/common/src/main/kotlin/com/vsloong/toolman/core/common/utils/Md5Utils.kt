package com.vsloong.toolman.core.common.utils

import java.io.File
import java.io.FileInputStream
import java.nio.file.Path
import java.security.DigestInputStream
import java.security.MessageDigest

/**
 * 获取文件的MD5值
 */
fun getFileMD5(path: Path): String {
    return getFileMD5(file = path.toFile())
}

fun getFileMD5(file: File): String {
    val md = MessageDigest.getInstance("MD5")
    val fis = FileInputStream(file)
    val dis = DigestInputStream(fis, md)

    val buffer = ByteArray(1024 * 8)
    while (dis.read(buffer) != -1) {
        // 不做处理，只是读取文件内容，用于更新MessageDigest
    }

    val digest = md.digest()

    val result = StringBuilder()
    for (byte in digest) {
        result.append(String.format("%02x", byte))
    }
    dis.close()

    return result.toString()
}