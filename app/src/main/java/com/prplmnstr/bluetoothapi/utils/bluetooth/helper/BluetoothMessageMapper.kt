package com.prplmnstr.bluetoothapi.utils.bluetooth.helper

import com.prplmnstr.bluetoothapi.model.bluetooth.BluetoothMessage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage {
    val (senderName, senderAddress, date, time, message) = split("#@")
        .map { it.replace("#@", "") }
    return BluetoothMessage.TextMessage(
        text = message,
        senderName = senderName,
        senderAddress = senderAddress,
        date = date,
        time = time,
        isFromLocalUser = isFromLocalUser
    )
}

fun BluetoothMessage.TextMessage.toByteArray(): ByteArray {
    return "$senderName#@$senderAddress#@$date#@$time#@$text".encodeToByteArray()
}


fun ByteArray.toBluetoothAudioMessage(isFromLocalUser: Boolean): BluetoothMessage {
    val inputStream = ByteArrayInputStream(this)
    val dataInputStream = DataInputStream(inputStream)

    // Read sender name
    val senderName = dataInputStream.readUTF()
    // Read sender address
    val senderAddress = dataInputStream.readUTF()
    // Read date
    val date = dataInputStream.readUTF()
    // Read time
    val time = dataInputStream.readUTF()
    // Read audio data length
    val audioLength = dataInputStream.readInt()
    // Read audio data
    val audioBytes = ByteArray(audioLength)
    dataInputStream.read(audioBytes)

    dataInputStream.close()

    return BluetoothMessage.AudioMessage(

        audioData = audioBytes,
        senderName = senderName,
        senderAddress = senderAddress,
        date = date,
        time = time,
        isFromLocalUser = isFromLocalUser
    )
}


fun BluetoothMessage.AudioMessage.toByteArray(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    val dataOutputStream = DataOutputStream(outputStream)

    // Write sender name
    dataOutputStream.writeUTF(senderName)
    // Write sender address
    dataOutputStream.writeUTF(senderAddress)
    // Write date
    dataOutputStream.writeUTF(date)
    // Write time
    dataOutputStream.writeUTF(time)
    // Write audio data length
    dataOutputStream.writeInt(audioData.size)
    // Write audio data
    dataOutputStream.write(audioData)

    dataOutputStream.flush()
    dataOutputStream.close()

    return outputStream.toByteArray()
}