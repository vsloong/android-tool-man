package com.vsloong.toolman.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen

interface BaseScreen : Screen

@Composable
inline fun <reified T : ScreenModel> BaseScreen.rememberViewModel(
    tag: String? = null,
    crossinline factory: @DisallowComposableCalls () -> T
): T {
    return rememberScreenModel(tag, factory)
}

