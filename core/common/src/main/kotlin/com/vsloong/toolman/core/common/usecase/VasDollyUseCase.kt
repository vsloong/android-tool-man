package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.usecase.interfaces.IChannelUseCase
import com.vsloong.toolman.core.common.usecase.interfaces.ICmdUseCase
import com.vsloong.toolman.core.common.utils.exec
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

class VasDollyUseCase(
    private val assetsManager: IAssetsPath,
) : IChannelUseCase, ICmdUseCase {

    override fun putChannel(
        apkPath: Path,
        channels: Set<String>
    ) {
        val apkName = apkPath.nameWithoutExtension
        channels.forEach { channel ->
//            run(
//                cmd = "${cmdName()} put -c $channel -f $apkPath " +
//                        "${apkPath.parent.resolve("${apkName}_${channel}.apk")}"
//            )

            val outputFile = apkPath.parent.resolve("${apkName}_${channel}.apk")

            // VasDolly需要先创建文件
            if (!Files.exists(outputFile)) {
                Files.createFile(outputFile)
            }

            exec(
                "java",
                "-jar",
                assetsManager.getVasDollyJarPath().toString(),
                "put",
                "-c",
                channel,
                "-f", //FastMode，生成渠道包时不进行强校验，速度可提升10倍以上
                apkPath.toString(),
                outputFile.toString(),
            )
        }
    }

    /**
     * 获取渠道信息：VasDolly输出信息如下：
     * Channel: xiaomi,len=6
     */
    override fun getChannel(
        apkPath: Path,
    ): String? {

        var lastLine = ""
        run(cmd = "${cmdName()} get -c $apkPath",
            onLine = {
                lastLine = it
            })

        val regex = Regex("""Channel:.*,len""")
        val result = regex.find(lastLine)?.value
        return result?.replace("Channel:", "")
            ?.replace(",len", "")
            ?.trim()
    }

    override fun removeChannel(apkPath: Path) {
        run(cmd = "${cmdName()} remove -c $apkPath")
    }

    override fun cmdName(): String {
        return CmdType.VasDolly.cmdName
    }

    override fun cmdPath(): String {
        return "java -jar ${assetsManager.getVasDollyJarPath()}"
    }
}