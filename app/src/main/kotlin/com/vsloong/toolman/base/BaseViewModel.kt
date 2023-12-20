package com.vsloong.toolman.base

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope

open class BaseViewModel : ScreenModel {
    val viewModelScope = screenModelScope
}