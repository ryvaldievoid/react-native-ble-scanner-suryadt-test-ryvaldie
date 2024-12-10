package com.atech.android.data.repositoriesimpl

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.atech.android.base.util.BleUtils
import com.atech.android.data.dtos.DeviceDataDtoItem
import javax.inject.Inject

class BleRepository @Inject constructor(
    private val context: Context,
    private val bleUtils: BleUtils
) {

    fun isBluetoothEnabled(): Boolean {
        return bleUtils.isBluetoothEnabled()
    }

    fun scanDevices(callback: (List<DeviceDataDtoItem>?) -> Unit) {
        bleUtils.startScan { devices ->
            callback(devices)
        }
    }

    fun stopScan() {
        bleUtils.stopScan()
    }

    fun connectToDevice(device: DeviceDataDtoItem, callback: (Boolean, String) -> Unit) {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter?.let {
            bleUtils.connectToDevice(
               it.getRemoteDevice(device.address)!!,
                callback
            )
        }
    }

    fun disconnectDevice() {
        bleUtils.disconnectFromDevice()
    }
}