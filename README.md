---

# **BLE Scanner and Connector App**

A **Bluetooth Low Energy (BLE) Scanner and Connector App** built with **Kotlin**, **MVVM architecture**, and **Hilt for Dependency Injection**. This app allows users to scan for nearby BLE devices, connect to a selected device, view its details, and disconnect when needed. It is designed for a clean architecture and supports modern Android development practices.

---

## **Features**

- Scan for nearby BLE devices.
- Display scanned devices in a user-friendly list.
- Pull-to-refresh for rescanning.
- Connect to a selected BLE device.
- View device details in a dialog.
- Disconnect from a connected device.
- Ensures proper runtime permission handling for BLE scanning and connecting.
- Handles Bluetooth state (turn on/off) with user prompts.

---

## **Technologies Used**

- **Kotlin**: For concise and expressive code.
- **MVVM (Model-View-ViewModel)**: For a clean separation of concerns and better testability.
- **Hilt**: For Dependency Injection.
- **LiveData and ViewModel**: For reactive UI updates.
- **RecyclerView**: For displaying the list of BLE devices.
- **SwipeRefreshLayout**: For pull-to-refresh functionality.
- **BluetoothAdapter**: For managing Bluetooth operations.
- **BluetoothGatt**: For handling device connections and GATT services.

---

## **Prerequisites**

1. **Android Studio**: Version `4.1` or later.
2. **Minimum SDK**: 21 (Android 5.0 Lollipop).
3. **Permissions**:
  - Bluetooth permissions (`BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, etc.).
  - Location permissions (`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`).

---

## **Installation**

1. Clone the repository:
   ```bash
   git clone https://github.com/ryvaldievoid/surya-dt-test-ble-app.git
   cd surya-dt-test-ble-app
   ```

2. Open the project in Android Studio.

3. Sync Gradle files and build the project:
  - Go to **File > Sync Project with Gradle Files**.

4. Run the app:
  - Connect a physical device or start an emulator.
  - Select **Run > Run App** in Android Studio.

---

## **Usage**

1. **Scanning for Devices**:
  - Open the app.
  - Pull down to refresh the list of available BLE devices or tap SCAN button.
  - The app automatically requests Bluetooth and location permissions.

2. **Connecting to a Device**:
  - Tap on a listed device to open the device details dialog.
  - Click **Connect** to establish a connection.

3. **Viewing Device Details**:
  - The details dialog displays the name and address of the device.
  - Options include **Connect**, **Disconnect**, or **Cancel**.

4. **Disconnecting from a Device**:
  - If connected, tap **Disconnect** in the device dialog to terminate the connection.

---

## **Code Overview**

### **1. Architecture**
The app is built using the MVVM pattern for separation of concerns:
- **Model**: Handles the business logic and data access (e.g., `BleRepository`).
- **View**: Displays the UI and delegates user interactions to the ViewModel (e.g., `HomeFragment`).
- **ViewModel**: Acts as a bridge between the Model and View, exposing LiveData for the UI to observe (e.g., `HomeViewModel`).

### **2. Key Classes**
- `HomeFragment`: Manages the UI, permissions, and user interactions.
- `HomeViewModel`: Handles state management for scanning and connecting to BLE devices.
- `BleRepository`: Provides an abstraction for BLE operations.
- `BleUtils`: Manages low-level BLE operations like scanning, connecting, and disconnecting.

### **3. Permissions**
The app dynamically requests the following permissions:
- `BLUETOOTH_SCAN`
- `BLUETOOTH_CONNECT`
- `ACCESS_FINE_LOCATION`

### **4. Dependencies**
- **Hilt**: Dependency Injection for cleaner code.
- **RecyclerView**: To display the list of BLE devices.
- **LiveData**: Reactive UI updates.

---

## **Future Improvements**
1. Add support for exploring GATT services and characteristics.
3. Enhance the UI with Material Design components.
4. Implement unit and integration tests.

---