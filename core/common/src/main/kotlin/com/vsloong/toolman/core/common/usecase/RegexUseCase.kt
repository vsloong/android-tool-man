package com.vsloong.toolman.core.common.usecase

class RegexUseCase {

    /**
     * 解析如下数据：
     * 988a5b35563658344f     device
     * f0df87a1               device
     *
     * 序列号：adb 会创建一个字符串，用于通过端口号唯一标识设备。下面是一个序列号示例：emulator-5554
     * 状态：设备的连接状态可以是以下几项之一：
     * offline：设备未连接到 adb 或没有响应。
     * device：设备已连接到 adb 服务器。请注意，此状态并不表示 Android 系统已完全启动并可正常运行，因为在设备连接到 adb 时系统仍在启动。系统完成启动后，设备通常处于此运行状态。
     * no device：未连接任何设备。
     * 说明：如果您加入 -l 选项，devices 命令会告知您设备是什么。当您连接了多个设备时，此信息会很有用，方便您区分这些设备。
     */
    fun getDeviceId(info: String): String? {
        val regex = Regex("""^\S+""")
        return regex.find(info)?.value
    }

    fun getDeviceModel(info: String): String? {
        val model = "model:"
        val regex = Regex("""${model}\S+""")
        return regex.find(info)?.value?.replace(model, "")
    }
}