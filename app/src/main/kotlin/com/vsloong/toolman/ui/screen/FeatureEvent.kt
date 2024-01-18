package com.vsloong.toolman.ui.screen

data class FeatureEvent(
    val onFeatureClick: (ToolManFeature) -> Unit,
)