package com.atech.android.feature.home

sealed class PermissionState {
    data object Granted : PermissionState()
    data object Denied : PermissionState()
}

sealed class BluetoothState {
    data object On : BluetoothState()
    data object Off : BluetoothState()
}