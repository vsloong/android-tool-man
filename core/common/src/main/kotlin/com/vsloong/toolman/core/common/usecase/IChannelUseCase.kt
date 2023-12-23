package com.vsloong.toolman.core.common.usecase

import java.nio.file.Path

interface IChannelUseCase {
    fun putChannel(apkPath: Path, channels: Set<String>)
    fun getChannel(apkPath: Path): String?
    fun removeChannel(apkPath: Path)
}