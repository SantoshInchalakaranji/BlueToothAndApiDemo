package com.prplmnstr.bluetoothchat.utils.bluetooth

import com.prplmnstr.bluetoothchat.model.bluetooth.BluetoothDeviceDomain
import com.prplmnstr.bluetoothchat.model.bluetooth.BluetoothMessage

sealed interface ConnectionResult {
    data class ConnectionEstablished(val peerDevice: BluetoothDeviceDomain) : ConnectionResult
    data class TransferSucceeded(val message: BluetoothMessage) : ConnectionResult
    data class Error(val message: String) : ConnectionResult
}