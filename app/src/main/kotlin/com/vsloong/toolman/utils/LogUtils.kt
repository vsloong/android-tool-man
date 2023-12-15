package com.vsloong.toolman.utils


fun logger(msg: String) {
    println("${Thread.currentThread().name} ${msg}")
}