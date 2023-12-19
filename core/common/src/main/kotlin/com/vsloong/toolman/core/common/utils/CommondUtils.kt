package com.vsloong.toolman.core.common.utils

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.Charset

/**
 * 执行cmd指令
 * @param cmd list形式的指令
 */
fun exec(
    cmd: List<String>,
    directory: File? = null,
    onMessage: (String) -> Unit = {},
    onComplete: (Int) -> Unit = {}
) = execute(
    cmd = cmd,
    directory = directory,
    onMessage = onMessage,
    onComplete = onComplete
)

/**
 * 执行cmd指令
 * @param cmd 数组形式的指令
 */
fun exec(
    vararg cmd: String,
    directory: File? = null,
    onMessage: (String) -> Unit = {},
    onComplete: (Int) -> Unit = {}
) = exec(
    cmd = listOf(*cmd),
    directory = directory,
    onMessage = onMessage,
    onComplete = onComplete
)


private fun execute(
    cmd: List<String>,
    directory: File? = null,
    onMessage: (String) -> Unit = {},
    onComplete: (Int) -> Unit = {}
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
    onComplete.invoke(result)
}
