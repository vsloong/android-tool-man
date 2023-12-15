package com.vsloong.toolman.ui.widget.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun int2px(size: Int): Int = with(LocalDensity.current) { size.dp.toPx().toInt() }

@Composable
fun dp2px(dp: Dp): Float = with(LocalDensity.current) { dp.toPx() }