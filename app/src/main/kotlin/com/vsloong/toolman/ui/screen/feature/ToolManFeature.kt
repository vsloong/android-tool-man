package com.vsloong.toolman.ui.screen.feature

sealed interface ToolManFeature {
    data object ScreenCap : ToolManFeature
    data object TopActivity:ToolManFeature
}