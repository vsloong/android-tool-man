package com.vsloong.toolman.core.common.model

import com.android.ddmlib.IDevice

class DeviceWrapper(device: IDevice) : IDevice by device {

    fun getPhoneName(): String {
        return name.replace(serialNumber, "")
    }


}