package com.vsloong.toolman.core.common.utils


fun logger(msg: String) {
    println("${Thread.currentThread().name} ${msg}")
}