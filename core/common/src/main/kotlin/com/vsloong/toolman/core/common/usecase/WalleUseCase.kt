package com.vsloong.toolman.core.common.usecase

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.utils.exec
import com.vsloong.toolman.core.common.utils.logger
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

class WalleUseCase(
    private val assetsManager: IAssetsPath
) : IChannelUseCase {

    private val jarPath = assetsManager.getWalleJarPath()

    override fun putChannel(apkPath: Path, channels: Set<String>) {
        val apkName = apkPath.nameWithoutExtension
        channels.forEach { channel ->
            exec(
                "java",
                "-jar",
                jarPath.toString(),
                "put",
                "-c",
                channel,
                "-e",
                "akey=avalue,hash=0xsdsd,channel2=a{s},d",
                apkPath.toString(),
                apkPath.parent.resolve("${apkName}_${channel}.apk").toString()
            )
        }

    }

    override fun getChannel(apkPath: Path): String? {
        var lastLine = ""
        exec(
            "java",
            "-jar",
            jarPath.toString(),
            "show",
            apkPath.toString(),
            onLine = {
                lastLine = it
            }
        )

        val regex = Regex("""\{.*}""")
        val result = regex.find(lastLine)?.value ?: return null
        val keyValue = result.substring(1, result.length - 1)
        val channelKeyValue = keyValue.split(",")
            .find {
                it.contains("channel=")
            } ?: return null

        return channelKeyValue
            .replace("channel=", "")
            .trim()
    }

    override fun removeChannel(apkPath: Path) {
        exec(
            "java",
            "-jar",
            jarPath.toString(),
            "rm",
            apkPath.toString()
        )
    }

}