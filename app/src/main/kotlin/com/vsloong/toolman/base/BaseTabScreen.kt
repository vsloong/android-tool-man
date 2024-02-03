package com.vsloong.toolman.base

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

abstract class BaseTabScreen : Tab, ITabOptions {


    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 0u,
            title = "",
            icon = null
        )

}