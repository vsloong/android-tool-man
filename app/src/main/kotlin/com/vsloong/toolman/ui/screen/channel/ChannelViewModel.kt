package com.vsloong.toolman.ui.screen.channel

import androidx.compose.runtime.mutableStateOf
import com.vsloong.toolman.base.BaseViewModel
import com.vsloong.toolman.core.common.usecase.VasDollyUseCase
import com.vsloong.toolman.core.common.usecase.WalleUseCase
import com.vsloong.toolman.core.common.usecase.interfaces.IChannelUseCase
import com.vsloong.toolman.core.common.utils.logger
import com.vsloong.toolman.manager.AssetsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.Files
import kotlin.io.path.Path

class ChannelViewModel(
    private val vasDollyUseCase: IChannelUseCase = VasDollyUseCase(assetsManager = AssetsManager),
    private val walleUseCase: IChannelUseCase = WalleUseCase(assetsManager = AssetsManager)
) : BaseViewModel() {

    private val channelUseCaseMap = mapOf(
        "VasDolly" to vasDollyUseCase,
        "Walle" to walleUseCase
    )

    val channelToolType = channelUseCaseMap.keys

    /**
     * 选择的APK和渠道类型
     */
    val selectApk = mutableStateOf(Path(""))
    val selectChannelToolType = mutableStateOf(channelToolType.first())


    /**
     * 查询包的渠道信息
     */
    val currentChannel = mutableStateOf("")

    val channelEvent = ChannelEvent(
        onPutChannel = {
            putChannel(it)
        },
        onSelectApk = {
            if (it.isNotEmpty()) {
                selectApk.value = it.first()
            }
        },
        onSelectChannelTool = {
            selectChannelToolType.value = it
        },
        onGetChannel = {
            getChannel()
        },
        onRemoveChanel = {
            removeChannel()
        }
    )

    private fun putChannel(channel: String) {
        if (!checkToNext()) {
            return
        }

        if (channel.isEmpty()) {
            logger("请输入渠道信息")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val channels = channel.split(",").toSet()
            getChannelUseCase()?.putChannel(selectApk.value, channels = channels)
        }

    }

    private fun getChannel() {
        if (!checkToNext()) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val channel = getChannelUseCase()?.getChannel(selectApk.value)

            channel?.let {
                currentChannel.value = it
            }
        }
    }

    private fun removeChannel() {
        if (!checkToNext()) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            getChannelUseCase()?.removeChannel(selectApk.value)
        }
    }

    private fun getChannelUseCase(): IChannelUseCase? {
        return channelUseCaseMap[selectChannelToolType.value]
    }

    private fun checkToNext(): Boolean {
        if (selectChannelToolType.value.isEmpty()) {
            logger("请选择打包渠道工具类型")
            return false
        }

        if (selectApk.value.toString().isEmpty() || !Files.exists(selectApk.value)) {
            logger("请选择APK文件")
            return false
        }

        return true
    }


}