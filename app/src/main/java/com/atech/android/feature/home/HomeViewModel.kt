package com.atech.android.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.atech.android.data.dtos.DeviceDataDtoItem
import com.atech.android.data.repositoriesimpl.BleRepository
import com.atech.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bleRepository: BleRepository
) : BaseViewModel() {

    private val _permissionState = MutableLiveData<PermissionState>()
    val permissionState: LiveData<PermissionState> = _permissionState

    private val _bluetoothState = MutableLiveData<BluetoothState>()
    val bluetoothState: LiveData<BluetoothState> = _bluetoothState

    private val _scanResults = MutableLiveData<List<DeviceDataDtoItem>?>()
    val scanResults: LiveData<List<DeviceDataDtoItem>?> = _scanResults

    private val _connectionStatus = MutableLiveData<String>()
    val connectionStatus: LiveData<String> = _connectionStatus

    private val _connectedDevice = MutableLiveData<DeviceDataDtoItem?>()
    val connectedDevice: LiveData<DeviceDataDtoItem?> = _connectedDevice

    fun onPermissionsGranted() {
        _permissionState.value = PermissionState.Granted
    }

    fun onPermissionsDenied() {
        _permissionState.value = PermissionState.Denied
    }

    fun checkBluetoothState() {
        if (bleRepository.isBluetoothEnabled()) {
            _bluetoothState.value = BluetoothState.On
        } else {
            _bluetoothState.value = BluetoothState.Off
        }
    }

    fun onBluetoothTurnedOn() {
        _bluetoothState.value = BluetoothState.On
    }

    fun onBluetoothTurnedOff() {
        _bluetoothState.value = BluetoothState.Off
    }

    fun startBleScan() {
        bleRepository.scanDevices { devices ->
            _scanResults.postValue(devices)
        }
    }

    fun stopBleScan() {
        bleRepository.stopScan()
    }

    fun connectToDevice(device: DeviceDataDtoItem) {
        bleRepository.connectToDevice(device) { success, message ->
            _connectionStatus.postValue(message)
            if (success) {
                _connectedDevice.postValue(device)
            } else {
                _connectedDevice.postValue(null)
            }
        }
    }

    fun disconnectDevice() {
        bleRepository.disconnectDevice()
        _connectedDevice.postValue(null)
        _connectionStatus.postValue("Disconnected")
    }
}
