package com.vsloong.toolman.ui.themes.colors

import androidx.compose.ui.graphics.Color

sealed class ThemeColors(
    val background: Color,
    val primary: Color,
    val text_primary:Color,
) {

    object Dark : ThemeColors(
        background = Color(0xFF110F17),
        primary = Color(0xFF7776FF),
        text_primary = Color(0xFFA2A0B1),
    )

    object Light : ThemeColors(
        background = Color.White,
        primary = Color.Black,
        text_primary = Color.Black,
    )
}