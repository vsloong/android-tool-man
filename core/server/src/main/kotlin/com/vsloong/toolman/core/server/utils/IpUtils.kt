package com.vsloong.toolman.core.server.utils

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface


/**
 * 根据网卡获得IP地址
 * 解决linux、mac下使用
 * InetAddress.getLocalHost().hostAddress
 * Inet4Address.getLocalHost().hostAddress
 * 获取地址是127.0.0.1的问题
 */
fun getIpAddress(): String {
    try {
        var candidateAddress: InetAddress? = null
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()

            // 该网卡接口下的ip会有多个，也需要一个个的遍历，找到自己所需要的
            val inetAddresses = networkInterface.inetAddresses
            while (inetAddresses.hasMoreElements()) {
                val inetAddress = inetAddresses.nextElement()
                // 排除loopback回环类型地址（不管是IPv4还是IPv6 只要是回环地址都会返回true）
                if (!inetAddress.isLoopbackAddress) {
                    if (inetAddress.isSiteLocalAddress) {
                        // 如果是site-local地址，就是它了 就是我们要找的
                        // 绝大部分情况下都会在此处返回你的ip地址值
                        return inetAddress.hostAddress
                    }

                    // 若不是site-local地址 那就记录下该地址当作候选
                    if (candidateAddress == null) {
                        candidateAddress = inetAddress
                    }
                }
            }
        }

        // 如果出去loopback回环地之外无其它地址了，那就回退到原始方案吧
        if (candidateAddress == null) {
            return Inet4Address.getLocalHost().hostAddress
        }
        return candidateAddress.hostAddress
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "127.0.0.1"
}