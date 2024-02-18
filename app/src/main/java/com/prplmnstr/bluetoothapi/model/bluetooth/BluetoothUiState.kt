package com.prplmnstr.bluetoothapi.model.bluetooth

/**
 * Represents the UI state related to Bluetooth functionality.
 */

data class BluetoothUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val messages: List<BluetoothMessage> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    var peerDevice: BluetoothDeviceDomain? = null

)