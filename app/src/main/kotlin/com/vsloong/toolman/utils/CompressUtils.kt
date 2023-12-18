package com.vsloong.toolman.utils

import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

/**
 * 解压缩 ZIP 文件到当前目录，文件夹名同文件名（无后缀）

 * @param zipFilePath ZIP 文件路径
 */
fun unzip(
    zipFilePath: Path
) {
    val dirName = zipFilePath.nameWithoutExtension
    val destDirPath = zipFilePath.parent.resolve(dirName)

    unzip(
        zipFilePath = zipFilePath,
        destDirPath = destDirPath
    )
}

/**
 * 解压缩 ZIP 文件到指定目录
 *
 * @param zipFilePath ZIP 文件路径
 * @param destDirPath 解压缩目标目录
 */
fun unzip(
    zipFilePath: Path,
    destDirPath: Path
) {
    val file = zipFilePath.toFile()
    if (!Files.exists(destDirPath)) {
        Files.createDirectories(destDirPath)
    }

    FileInputStream(file).use { fileInputStream ->
        ZipArchiveInputStream(fileInputStream).use { zipInputStream ->
            var entry: ArchiveEntry? = zipInputStream.nextEntry
            while (entry != null) {
                val destFile = File(destDirPath.toFile(), entry.name)
                if (entry.isDirectory) {
                    destFile.mkdirs()
                } else {
                    destFile.parentFile.mkdirs()
                    FileOutputStream(destFile).use { fileOutputStream ->
                        zipInputStream.copyTo(fileOutputStream)
                    }
                }
                entry = zipInputStream.nextEntry
            }
        }
    }
}