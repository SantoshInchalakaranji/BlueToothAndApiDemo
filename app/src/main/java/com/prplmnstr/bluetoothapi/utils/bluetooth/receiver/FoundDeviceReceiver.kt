package com.prplmnstr.bluetoothapi.utils.bluetooth.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class FoundDeviceReceiver(
    private val onDeviceFound: (BluetoothDevice) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("TAG", "onReceive: running")
        when (intent?.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Log.e("TAG", "DEVICE FOUND : yes ")
                    intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_DEVICE,
                        BluetoothDevice::class.java
                    )
                } else {
                    Log.e("TAG", "DEVICE FOUND : yes ")
                    intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_DEVICE
                    )
                }
                device?.let(onDeviceFound)
            }
        }
    }
}