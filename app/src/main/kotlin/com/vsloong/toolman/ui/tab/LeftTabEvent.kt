package com.vsloong.toolman.ui.tab

data class LeftTabEvent(
    val onHomeClick: () -> Unit,
    val onFeatureClick: () -> Unit,
)