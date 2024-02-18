package com.prplmnstr.bluetoothchat.utils.bluetooth.helper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.prplmnstr.bluetoothchat.model.bluetooth.BluetoothDeviceDomain


@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}