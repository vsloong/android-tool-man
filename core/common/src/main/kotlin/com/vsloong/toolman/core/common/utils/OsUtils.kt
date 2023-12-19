package com.vsloong.toolman.core.common.utils

fun getCurrentOs(): String {
    return System.getProperty("os.name").lowercase()
}

fun isMacOs(): Boolean {
    return getCurrentOs().startsWith("mac")
}

fun isWindows(): Boolean {
    return getCurrentOs().startsWith("windows")
}

fun isLinux(): Boolean {
    return getCurrentOs().startsWith("linux")
}