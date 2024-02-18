package com.prplmnstr.bluetoothapi.utils.bluetooth

import com.prplmnstr.bluetoothapi.model.bluetooth.BluetoothDeviceDomain
import com.prplmnstr.bluetoothapi.model.bluetooth.BluetoothMessage

sealed interface ConnectionResult {
    data class ConnectionEstablished(val peerDevice: BluetoothDeviceDomain) : ConnectionResult
    data class TransferSucceeded(val message: BluetoothMessage) : ConnectionResult
    data class Error(val message: String) : ConnectionResult
}