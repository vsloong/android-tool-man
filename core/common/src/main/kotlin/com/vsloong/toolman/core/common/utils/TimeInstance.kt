package com.vsloong.toolman.core.common.utils

import java.text.SimpleDateFormat

object TimeInstance {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

    fun getPrinterTime(): String {
        return formatter.format(System.currentTimeMillis())
    }
}