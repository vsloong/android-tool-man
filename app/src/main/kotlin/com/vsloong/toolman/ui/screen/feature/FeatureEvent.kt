package com.vsloong.toolman.ui.screen.feature

data class FeatureEvent(
    val onFeatureClick: (ToolManFeature) -> Unit,
)