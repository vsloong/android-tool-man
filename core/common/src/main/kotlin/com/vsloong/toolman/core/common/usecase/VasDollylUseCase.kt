package com.vsloong.toolman.core.common.usecase

import com.vsloong.toolman.core.common.manager.IAssetsPath
import com.vsloong.toolman.core.common.utils.exec
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

class VasDollylUseCase(
    private val assetsManager: IAssetsPath,
) : IChannelUseCase {

    private val jarPath = assetsManager.getVasDollyJarPath()

    override fun putChannel(
        apkPath: Path,
        channels: Set<String>
    ) {
        val apkName = apkPath.nameWithoutExtension
        channels.forEach { channel ->
            exec(
                "java",
                "-jar",
                jarPath.toString(),
                "put",
                "-c",
                channel,
                "-f", //FastMode，生成渠道包时不进行强校验，速度可提升10倍以上
                apkPath.toString(),
                apkPath.parent.resolve("${apkName}_${channel}.apk").toString(),
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
        exec(
            "java",
            "-jar",
            jarPath.toString(),
            "get",
            "-c",
            apkPath.toString(),
            onLine = {
                lastLine = it
            }
        )

        val regex = Regex("""Channel:.*,len""")
        val result = regex.find(lastLine)?.value
        return result?.replace("Channel:", "")
            ?.replace(",len", "")
            ?.trim()
    }

    override fun removeChannel(apkPath: Path) {
        exec(
            "java",
            "-jar",
            jarPath.toString(),
            "remove",
            "-c",
            apkPath.toString(),
        )
    }
}