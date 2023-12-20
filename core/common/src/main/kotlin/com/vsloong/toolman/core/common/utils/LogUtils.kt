package com.vsloong.toolman.core.common.utils


fun logger(msg: String) {
    println("${TimeInstance.getPrinterTime()} ${Thread.currentThread().name} --- $msg")
}