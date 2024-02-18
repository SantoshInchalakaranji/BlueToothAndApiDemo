package com.prplmnstr.bluetoothapi.model.bluetooth


sealed class BluetoothMessage {
    data class TextMessage(
        val text: String,
        val date: String,
        val time: String,
        val senderName: String,
        var senderAddress: String,
        val isFromLocalUser: Boolean
    ) : BluetoothMessage()

    data class ImageMessage(
        val imageData: ByteArray,
        val date: String,
        val time: String,
        val senderName: String,
        val senderAddress: String,
        val isFromLocalUser: Boolean
    ) : BluetoothMessage()

    data class AudioMessage(
        val audioData: ByteArray,
        val date: String,
        val time: String,
        val senderName: String,
        var senderAddress: String,
        val isFromLocalUser: Boolean
    ) : BluetoothMessage()
}
