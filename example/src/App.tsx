import { useState, useEffect } from 'react';
import { Text, View, StyleSheet, Button, FlatList, PermissionsAndroid, Alert, type Permission, TouchableOpacity } from 'react-native';
import BleUtils, {type DeviceData, BluetoothState } from 'react-native-ble-scanner-suryadt-test-ryvaldie';

export default function App() {
  const [bluetoothState, setBluetoothState] = useState<string>();
  const [devices, setDevices] = useState<DeviceData[]>([]);

  const requestBluetoothPermission = async () => {
    try {
      const permissions: Permission[] = [
        PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN!,
        PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT!,
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION!, // For legacy support
      ];

      const granted = await PermissionsAndroid.requestMultiple(permissions);

      const allGranted = Object.values(granted).every(
        (status) => status === PermissionsAndroid.RESULTS.GRANTED
      );

      if (!allGranted) {
        Alert.alert(
          'Permissions Required',
          'BLE functionality requires Bluetooth and Location permissions. Please enable them in your settings.'
        );
        return false;
      }

      return true;
    } catch (error) {
      console.error('Permission request error:', error);
      return false;
    }
  };

  useEffect(() => {
    if (!BleUtils.isBluetoothEnabled()) {
      // Handle turn on bluetooth
      console.log("You need to turn on bluetooth")
    }
    requestBluetoothPermission();
    // Listen for state changes
    const bluetoothStateListener = BleUtils.onBluetoothStateChanged((state: string) => {
      console.log('Bluetooth state', state);
      setBluetoothState(state);
      // handle state changes
      if (state === BluetoothState.SCANNING_STOP) {
        // handle state changes
      }
    });
    // Listen for discovered devices
    const deviceListener = BleUtils.onDeviceFound((device: DeviceData[]) => {
      const filteredData = device.filter((item, index, self) => index === self.findIndex((mItem) => mItem.address === item.address));
      setDevices(filteredData);
    });
    return () => {
      bluetoothStateListener.remove();
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

  const stopScan = async () => {
    try {
      await BleUtils.stopScan();
    } catch (error) {
      console.log(error)
    }
  };

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

  const disconnectFromDevice = async () => {
    try {
      await BleUtils.disconnectFromDevice();
    } catch (error) {
      console.log(error)
    }
  };

  return (
    <View style={styles.container}>
      <Text>Bluetooth State: {bluetoothState}</Text>
      <Button title="Start Scan" onPress={startScan} />
      <Button title="Stop Scan" onPress={stopScan} />
      <Text>Discovered Devices:</Text>
      <FlatList
        data={devices}
        refreshing={true}
        keyExtractor={(item) => item.address}
        renderItem={({ item }) => (
          <TouchableOpacity
            onPress={() => {
              connectToDevice(item.address);
            }}
            key={item.address}
            style={styles.itemContainer}>
            <Text>
              {item.id || 'Unknown ID'}
            </Text>
            <Text>
              {item.name || 'Unknown Name'}
            </Text>
            <Text>
              Signal: {item.signal}
            </Text>
            <Text>
              Address: {item.address}
            </Text>
          </TouchableOpacity>
        )}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginHorizontal: 10
  },
  itemContainer: {
    width: '100%',
    padding: 10,
    flexDirection: 'column',
    borderWidth: 1,
    borderColor: 'black'
  }
});
