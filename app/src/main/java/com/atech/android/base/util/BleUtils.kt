package com.atech.android.base.util

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.atech.android.data.dtos.DeviceDataDtoItem
import com.atech.android.data.repositoriesimpl.BleRepository
import javax.inject.Inject

class BleUtils@Inject constructor(
    private val context: Context
) {
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private var bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner
    private var bluetoothGatt: BluetoothGatt? = null
    private var scanCallback: ScanCallback? = null

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    @SuppressLint("MissingPermission")
    fun startScan(callback: (List<DeviceDataDtoItem>?) -> Unit) {
        if (bluetoothLeScanner == null) {
            callback(null)
            return
        }

        val scanResults = mutableListOf<DeviceDataDtoItem>()
        scanCallback = object : ScanCallback() {
            @SuppressLint("MissingPermission")
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                scanResults.add(
                    DeviceDataDtoItem(
                        "",
                        result.device.name ?: "Unknown",
                        result.rssi.toString(),
                        result.device.address
                    )
                )
                callback(scanResults)
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            stopScan()
            callback(null)
        }, SCAN_PERIOD)

        bluetoothLeScanner?.startScan(scanCallback)
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        scanCallback?.let {
            bluetoothLeScanner?.stopScan(it)
            scanCallback = null
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice, callback: (Boolean, String) -> Unit) {
        bluetoothGatt = device.connectGatt(context, false, @SuppressLint("MissingPermission")
        object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    callback(true, "Connected to ${device.name}")
                    gatt.discoverServices() // Start discovering services
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    callback(false, "Disconnected from ${device.name}")
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
//                    val services = mutableListOf<String>()
//                    gatt.services.map { service ->
//                        services.add(ser)
//                    }
                    callback(true, "Services discovered for ${device.name}")
                } else {
                    callback(false, "Failed to discover services for ${device.name}")
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun disconnectFromDevice() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    companion object {
        private const val SCAN_PERIOD: Long = 10000
    }
}