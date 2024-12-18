### **React Native BLE Scanner Library README**

```markdown
# react-native-ble-scanner-suryadt-test-ryvaldie

A React Native module that facilitates scanning and interacting with Bluetooth Low Energy (BLE) devices. This library provides an interface for discovering BLE peripherals, managing connections, and retrieving GATT services. Createed as a test ssubmission for Surya Digital Teknologi.

---

## Features
- Discover BLE devices with signal strength and metadata.
- Connect to BLE devices and interact with their GATT services.
- Compatible with Android only.

---

## Compatibility
- **React Native**: 0.68+
- **Platforms**: Android (support for Android 12+ permissions included).

---

## Installation

Install the library using npm or yarn:

```bash
npm install git+https://github.com/ryvaldievoid/react-native-ble-scanner-suryadt-test-ryvaldie.git
```

Or with Yarn:

```bash
yarn add git+https://github.com/ryvaldievoid/react-native-ble-scanner-suryadt-test-ryvaldie.git
```

---

## Configuration

### Android
1. Add the following permissions to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

2. Update your app's `build.gradle` to include React Native dependencies if missing.

3. For Android 12+, ensure runtime permissions are handled correctly.

---

## Usage

### Scanning for BLE Devices
```typescript
import BleUtils, {type DeviceData, BluetoothState } from 'react-native-ble-scanner-suryadt-test-ryvaldie';

export default function App() {
  useEffect(() => {
    // Listen for discovered devices
    const deviceListener = BleUtils.onDeviceFound((device: DeviceData[]) => {
      const filteredData = device.filter((item, index, self) => index === self.findIndex((mItem) => mItem.address === item.address));
      setDevices(filteredData);
    });
    return () => {
      deviceListener.remove();
    };
  }, []);

  const startScan = async () => {
    try {
      setDevices([])
      await BleUtils.startScan();
    } catch (error) {
      console.log(error)
    }
  };
...
```

### Connecting to a Device & Retrieving GATT Services
```typescript
const connectToDevice = async (deviceAddress: string) => {
    try {
      const res = await BleUtils.connectToDevice(deviceAddress);
      let concatResponse = '';
      if (typeof res === 'string') {
        concatResponse = res;
      } else {
        res.forEach(service => {
          concatResponse += 'Service UUID:' + service.uuid + '\n';
          service.characteristics.forEach(characteristic => {
            concatResponse += 'Characteristic UUID:' + characteristic.uuid + '\n';
            concatResponse += 'Properties:' + characteristic.properties + '\n';
            characteristic.descriptors.forEach(descriptor => {
              concatResponse += 'Descriptor UUID:' + descriptor.uuid + '\n';
            });
          });
        });
      }

      Alert.alert(
        'Device Details',
        concatResponse
      );
    } catch (error) {
      console.log(error)
    }
  };
```

---

### For more detail check example App
