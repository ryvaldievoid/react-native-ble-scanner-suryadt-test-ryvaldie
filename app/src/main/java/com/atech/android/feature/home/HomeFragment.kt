package com.atech.android.feature.home

import BleDeviceAdapter
import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.atech.android.base.BaseFragment
import com.atech.android.data.dtos.DeviceDataDtoItem
import com.atech.android.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()
    override val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val bleDeviceAdapter = BleDeviceAdapter {
        // on click item
        showDeviceDetailsDialog(it)
    }

    private fun showDeviceDetailsDialog(device: DeviceDataDtoItem) {
        AlertDialog.Builder(requireContext())
            .setTitle(device.name)
            .setMessage("Address: ${device.address}")
            .setPositiveButton("Connect") { _, _ ->
                viewModel.connectToDevice(device)
            }
            .setNegativeButton("Disconnect") { _, _ ->
                viewModel.disconnectDevice()
            }
            .setNeutralButton("Cancel", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onInitViews() {
        super.onInitViews()
        checkAndRequestPermissions()

        binding.apply {
            rvDevices.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = bleDeviceAdapter
            }

            swRefresh.setOnRefreshListener {
                checkAndRequestPermissions()
            }

            btnScan.setOnClickListener {
                if (btnScan.text == "SCAN") {
                    checkAndRequestPermissions()
                } else {
                    swRefresh.isRefreshing = false
                    viewModel?.stopBleScan()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onInitObservers() {
        super.onInitObservers()
        // Observe ViewModel state for permissions
        viewModel.permissionState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PermissionState.Granted -> viewModel.checkBluetoothState()
                is PermissionState.Denied -> showPermissionDeniedDialog()
            }
        }

        // Observe ViewModel for Bluetooth state
        viewModel.bluetoothState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BluetoothState.Off -> showBluetoothEnableDialog()
                is BluetoothState.On -> viewModel.startBleScan()
            }
        }

        // Observe BLE scan results
        viewModel.scanResults.observe(viewLifecycleOwner) { devices ->
            // Update your UI with the scanned BLE devices
            if (devices == null) {
                // scan stop
                binding.apply {
                    swRefresh.isRefreshing = false
                    btnScan.text = "SCAN"
                }
            } else {
                binding.apply {
                    swRefresh.isRefreshing = true
                    btnScan.text = "STOP SCAN"
                }
                val filteredDevices = devices
                    .asReversed()
                    .distinctBy { it.address }
                    .asReversed()
                    .toMutableList()
                bleDeviceAdapter.submitList(filteredDevices)
            }
        }

        viewModel.connectionStatus.observe(viewLifecycleOwner) { status ->
            Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var isGranted = false
        permissions.entries.forEach {
            isGranted = it.value
        }
        if (isGranted) {
            viewModel.onPermissionsGranted()
        } else {
            viewModel.onPermissionsDenied()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permissions.all {
                ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
            }) {
            viewModel.onPermissionsGranted()
        } else {
            requestPermissionLauncher.launch(permissions)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permissions are required to scan and connect to BLE devices.")
            .setPositiveButton("Grant") { _, _ -> checkAndRequestPermissions() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private val requestBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            viewModel.onBluetoothTurnedOn()
        } else {
            viewModel.onBluetoothTurnedOff()
        }
    }

    private fun showBluetoothEnableDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Enable Bluetooth")
            .setMessage("Bluetooth must be turned on to scan for devices.")
            .setPositiveButton("Turn On") { _, _ ->
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetoothLauncher.launch(enableBtIntent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}