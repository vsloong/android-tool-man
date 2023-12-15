package com.vsloong.toolman.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.Charset

private fun execute(
    cmd: List<String>,
    directory: File? = null,
    onMessage: (String) -> Unit = {},
    onOver: (Int) -> Unit = {}
) {
    logger("cmd --->> ${cmd.joinToString(separator = " ")}")

    val process = ProcessBuilder(cmd)
        .directory(directory)
        .redirectErrorStream(true)
        .start()

    val reader = BufferedReader(
        InputStreamReader(
            process.inputStream,
            Charset.forName("UTF-8")
        )
    )

    var line: String?
    while (reader.readLine().also { line = it } != null) {
        line?.let { str ->
            logger(str)
            onMessage(str)
        }
    }

    val result = process.waitFor()
//    if (result != 0) {
//        throw Throwable("result code is not 0")
//    }
    onOver.invoke(result)
}

/**
 * 运行CMD指令
 */
suspend fun exec(
    cmd: List<String>,
    directory: File? = null,
    onMessage: (String) -> Unit = {},
    onOver: (Int) -> Unit = {}
) = withContext(Dispatchers.IO) {
    execute(
        cmd = cmd,
        directory = directory,
        onMessage = onMessage,
        onOver = onOver
    )
}

suspend fun exec(
    vararg cmd: String,
    directory: File? = null,
    onMessage: (String) -> Unit = {},
    onOver: (Int) -> Unit = {}
) = exec(
    cmd = listOf(*cmd),
    directory = directory,
    onMessage = onMessage,
    onOver = onOver
)