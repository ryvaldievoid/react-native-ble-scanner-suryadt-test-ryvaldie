package com.blescannersuryadttestryvaldie

import android.Manifest
import android.annotation.SuppressLint
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import androidx.core.app.ActivityCompat
import android.util.Log
import com.blescannersuryadttestryvaldie.dtos.DeviceDataDtoItem
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule

// TODO handle permission check in all function
class BleUtilsModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  private var bluetoothManager: BluetoothManager? = null
  private var bluetoothAdapter: BluetoothAdapter? = null
  private var bluetoothLeScanner: BluetoothLeScanner? = null
  private var bluetoothGatt: BluetoothGatt? = null
  private var scanCallback: ScanCallback? = null

  init {
    bluetoothManager = reactContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    bluetoothAdapter = bluetoothManager?.adapter
    bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
  }

  @ReactMethod
  fun isBluetoothEnabled(): Boolean {
     return if (bluetoothAdapter?.isEnabled == true) {
       sendEvent(BLUETOOTH_ON)
       true
    } else {
       sendEvent(BLUETOOTH_OFF)
       false
    }
  }

  @SuppressLint("MissingPermission")
  @ReactMethod
  fun startScan(promise: Promise) {
//    if (ActivityCompat.checkSelfPermission(
//        reactApplicationContext,
//        Manifest.permission.BLUETOOTH_SCAN
//      ) != PackageManager.PERMISSION_GRANTED
//    ) {
//      sendEvent(PERMISSION_DENIED)
//      promise.reject("PERMISSION_DENIED", "Permission denied: Bluetooth Scan")
//      return
//    } else {
//      sendEvent(PERMISSION_GRANTED)
//    }

    if (!isBluetoothEnabled()) {
      return
    }

    if (bluetoothLeScanner == null) {
      promise.reject("INITIALIZATION_ERROR", "Bluetooth scanner not initialize")
      return
    }

    val scanResults = mutableListOf<DeviceDataDtoItem>()
    scanCallback = object : ScanCallback() {
      @SuppressLint("MissingPermission")
      override fun onScanResult(callbackType: Int, result: ScanResult) {
//        if (ActivityCompat.checkSelfPermission(
//            reactApplicationContext,
//            Manifest.permission.BLUETOOTH_CONNECT
//          ) != PackageManager.PERMISSION_GRANTED
//        ) {
//          sendEvent(PERMISSION_DENIED)
//          promise.reject("PERMISSION_DENIED", "Permission denied: Bluetooth Connect")
//          return
//        } else {
//          sendEvent(PERMISSION_GRANTED)
//        }

        scanResults.add(
          DeviceDataDtoItem(
            "",
            result.device.name ?: "Unknown",
            result.rssi.toString(),
            result.device.address
          )
        )

        val exposedResult = Arguments.createArray().apply {
          scanResults.forEach { deviceDataDtoItem ->
            pushMap(deviceDataDtoItem.toWritableMap())
          }
        }

        sendEvent(exposedResult)
        promise.resolve(null)
      }
    }

    Handler(Looper.getMainLooper()).postDelayed({
      stopScan(promise)
      promise.resolve(null)
    }, SCAN_PERIOD)

    bluetoothLeScanner?.startScan(scanCallback)
    sendEvent(SCANNING_STARTED)
  }

  @SuppressLint("MissingPermission")
  @ReactMethod
  fun stopScan(promise: Promise) {
    scanCallback?.let {
      if (!isBluetoothEnabled()) {
        return
      }
//      if (ActivityCompat.checkSelfPermission(
//          reactApplicationContext,
//          Manifest.permission.BLUETOOTH_SCAN
//        ) != PackageManager.PERMISSION_GRANTED
//      ) {
//        sendEvent(PERMISSION_DENIED)
//        promise.reject("PERMISSION_DENIED", "Permission denied: Bluetooth Scan")
//        return
//      }
      bluetoothLeScanner?.stopScan(it)
      scanCallback = null
      sendEvent(SCANNING_STOP)
      promise.resolve(null)
    }
  }

  @SuppressLint("MissingPermission")
  @ReactMethod
  fun connectToDevice(deviceAddress: String, promise: Promise) {
    if (!isBluetoothEnabled()) {
      return
    }

    val device = bluetoothAdapter?.getRemoteDevice(deviceAddress)
    if (device == null) {
      promise.reject("DEVICE_NOT_FOUND", "Device with address $deviceAddress not found")
      return
    }

//    if (ActivityCompat.checkSelfPermission(
//        reactApplicationContext,
//        Manifest.permission.BLUETOOTH_CONNECT
//      ) != PackageManager.PERMISSION_GRANTED
//    ) {
//      sendEvent(PERMISSION_DENIED)
//      promise.reject("PERMISSION_DENIED", "Permission denied: Bluetooth Connect")
//      return
//    }
    bluetoothGatt = device.connectGatt(reactApplicationContext, false, @SuppressLint("MissingPermission")
    object : BluetoothGattCallback() {
      override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        if (newState == BluetoothProfile.STATE_CONNECTED) {
          sendEvent(DEVICE_CONNECTED)
          gatt.discoverServices() // Start discovering services
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
          sendEvent(DEVICE_DISCONNECTED)
          promise.resolve("Disconnected from ${device.address}")
        }
      }

      override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
          try {
            val gattServices = gatt.services ?: listOf()

            // Convert GATT services into an array
            val servicesArray = Arguments.createArray()

            for (service in gattServices) {
              val serviceMap = Arguments.createMap()
              serviceMap.putString("uuid", service.uuid.toString())

              // Convert characteristics into an array
              val characteristicsArray = Arguments.createArray()
              for (characteristic in service.characteristics) {
                val characteristicMap = Arguments.createMap()
                characteristicMap.putString("uuid", characteristic.uuid.toString())
                characteristicMap.putInt("properties", characteristic.properties)

                // Convert descriptors into an array
                val descriptorsArray = Arguments.createArray()
                for (descriptor in characteristic.descriptors) {
                  val descriptorMap = Arguments.createMap()
                  descriptorMap.putString("uuid", descriptor.uuid.toString())
                  descriptorsArray.pushMap(descriptorMap)
                }
                characteristicMap.putArray("descriptors", descriptorsArray)

                characteristicsArray.pushMap(characteristicMap)
              }

              serviceMap.putArray("characteristics", characteristicsArray)
              servicesArray.pushMap(serviceMap)
            }

            // Resolve the promise with the serialized array
            promise.resolve(servicesArray)
          } catch (e: Exception) {
            promise.reject("ERROR_FETCHING_GATT_SERVICES", e)
          }
        } else {
          promise.resolve("Disconnected from ${device.address}")
        }
      }
    })
  }

  @SuppressLint("MissingPermission")
  @ReactMethod
  fun disconnectFromDevice(promise: Promise) {
    if (!isBluetoothEnabled()) {
      return
    }

//    if (ActivityCompat.checkSelfPermission(
//        reactApplicationContext,
//        Manifest.permission.BLUETOOTH_CONNECT
//      ) != PackageManager.PERMISSION_GRANTED
//    ) {
//      promise.reject("PERMISSION_DENIED", "Permission denied: Bluetooth Connect")
//      return
//    }
    bluetoothGatt?.disconnect()
    bluetoothGatt?.close()
    bluetoothGatt = null
    sendEvent(DEVICE_DISCONNECTED)
    promise.resolve(null)
  }

  private fun sendEvent(params: WritableArray?) {
    reactApplicationContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(ON_DEVICES_FOUND, params)
  }

  private fun sendEvent(params: String?) {
    reactApplicationContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(BLUETOOTH_STATE, params)
  }

  @ReactMethod
  fun getBluetoothState(): String = BLUETOOTH_STATE

  @ReactMethod
  fun getDevices(): String = ON_DEVICES_FOUND

  companion object {
    const val NAME = "BleUtils"

    private const val SCAN_PERIOD: Long = 10000

    // BLE state
    const val BLUETOOTH_STATE = "BLUETOOTH_STATE"
    const val BLUETOOTH_OFF = "BLUETOOTH_OFF"
    const val BLUETOOTH_ON = "BLUETOOTH_ON"
    const val PERMISSION_DENIED = "PERMISSION_DENIED"
    const val PERMISSION_GRANTED = "PERMISSION_GRANTED"
    const val SCANNING_STARTED = "SCANNING_STARTED"
    const val SCANNING_STOP = "SCANNING_STOP"
    const val ON_DEVICES_FOUND = "DEVICES_FOUND"
    const val DEVICE_CONNECTED = "DEVICE_CONNECTED"
    const val DEVICE_DISCONNECTED = "DEVICE_DISCONNECTED"
    const val UNKNOWN_ERROR = "UNKNOWN_ERROR"
  }
}
