import { NativeEventEmitter, NativeModules } from 'react-native';

// Define the Native Module interface
interface BleUtilsModule {
  isBluetoothEnabled(): boolean;
  startScan(): Promise<null>;
  stopScan(): Promise<string>;
  connectToDevice(deviceAddress: string): Promise<string>;
  disconnectFromDevice(): void;
  getBluetoothState(): string;
  getDevices(): string;
}

// DTOs
export interface DeviceData {
  id: string;
  name: string | null;
  signal: string;
  address: string;
}

// State
export enum BluetoothState {
  BLUETOOTH_OFF = "BLUETOOTH_OFF",
  BLUETOOTH_ON = "BLUETOOTH_ON",
  PERMISSION_DENIED = "PERMISSION_DENIED",
  PERMISSION_GRANTED = "PERMISSION_GRANTED",
  SCANNING_STARTED = "SCANNING_STARTED",
  SCANNING_STOP = "SCANNING_STOP",
  ON_DEVICES_FOUND = "DEVICES_FOUND",
  DEVICE_CONNECTED = "DEVICE_CONNECTED",
  DEVICE_DISCONNECTED = "DEVICE_DISCONNECTED"
}

const { BleUtils } = NativeModules;
const bleEventEmitter = new NativeEventEmitter(BleUtils);

const BleUtilsNative: BleUtilsModule = BleUtils;

const BleUtilsWrapper = {
  /**
   * Check is device bluetooth is enabled.
   * @returns bluetooth status.
   */
  isBluetoothEnabled: () => {
    return BleUtilsNative.isBluetoothEnabled();
  },
  /**
   * Start bluetooth scanning.
   * Automatically stop after 10000L MILLIS
   * @returns devices found.
   */
  startScan: async (): Promise<null> => {
    try {
      const result = await BleUtilsNative.startScan();
      return result;
    } catch (error: any) {
      throw error;
    }
  },
  /**
   * Stop bluetooth scanning.
   * @returns devices found.
   */
  stopScan: async (): Promise<string> => {
    try {
      const result = await BleUtilsNative.stopScan();
      return result;
    } catch (error: any) {
      throw error;
    }
  },
  /**
   * Connects to a BLE device using its address.
   * @param deviceAddress - The Bluetooth MAC address of the device to connect to.
   * @returns A promise that resolves to a success message or rejects on error.
   */
  connectToDevice: async (deviceAddress: string): Promise<string> => {
    try {
      const result = await BleUtilsNative.connectToDevice(deviceAddress);
      return result;
    } catch (error: any) {
      throw error;
    }
  },
  /**
   * Disconnects the currently connected BLE device.
   */
  disconnectFromDevice: () => {
    try {
      BleUtilsNative.disconnectFromDevice();
    } catch (error: any) {
      throw error;
    }
  },
  /**
   * Listen to all bluetooth state changes.
   */
  onBluetoothStateChanged: (callback: (state: string) => void) => {
    const eventType = BleUtilsNative.getBluetoothState();
    return bleEventEmitter.addListener(
      eventType,
      callback
    )
  },
  /**
   * Listen to on device found.
   */
  onDeviceFound: (callback: (device: DeviceData[]) => void) => {
    const eventType = BleUtilsNative.getDevices();
    return bleEventEmitter.addListener(
      eventType,
      callback
    )
  },
};

export default BleUtilsWrapper;