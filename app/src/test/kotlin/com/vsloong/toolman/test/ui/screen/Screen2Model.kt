package com.vsloong.toolman.test.ui.screen

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.vsloong.toolman.core.common.utils.logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Screen2Model : ScreenModel {

    fun log() {
        screenModelScope.launch {
            for (index in 1..10) {
                delay(1000)
                logger("Screen1 -> $index")
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
        logger("Screen2Model onDispose")
    }
}