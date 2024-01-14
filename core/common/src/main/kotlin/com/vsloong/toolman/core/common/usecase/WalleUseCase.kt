package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.usecase.interfaces.IChannelUseCase
import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase
import com.vsloong.toolman.core.common.utils.exec
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

class WalleUseCase(
    private val assetsManager: IAssetsPath
) : IChannelUseCase, ICmdUseCase {

    override fun putChannel(apkPath: Path, channels: Set<String>) {
        val apkName = apkPath.nameWithoutExtension
        channels.forEach { channel ->
            run(cmd = "${cmdName()} put -c $channel $apkPath ${apkPath.parent.resolve("${apkName}_${channel}.apk")}")
        }

    }

    override fun getChannel(apkPath: Path): String? {
        var lastLine = ""

        run(
            cmd = "${cmdName()} show $apkPath",
            onLine = {
                lastLine = it
            })

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
        run(cmd = "${cmdName()} rm $apkPath")
    }

    override fun cmdName(): String {
        return CmdConstant.Walle.cmdName
    }

    override fun cmdPath(): String {
        return "java -jar ${assetsManager.getWalleJarPath()}"
    }

}