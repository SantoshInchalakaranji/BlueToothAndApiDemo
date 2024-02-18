package com.prplmnstr.bluetoothchat.utils.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.prplmnstr.bluetoothchat.model.bluetooth.BluetoothMessage
import com.prplmnstr.bluetoothchat.utils.bluetooth.helper.toBluetoothMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class BluetoothDataTransferService(
    private val socket: BluetoothSocket
) {
    fun listenForIncomingMessages(): Flow<BluetoothMessage> {
        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            val buffer = ByteArray(1024)
            while (true) {
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch (e: IOException) {
                    Log.d("TAG", "transefer failed Error: $e+++++ ${e.cause}")
                    throw TransferFailedException()
                }

                emit(
                    buffer.decodeToString(
                        endIndex = byteCount
                    ).toBluetoothMessage(
                        isFromLocalUser = false
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendMessage(bytes: ByteArray): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket.outputStream.write(bytes)
            } catch (e: IOException) {
                Log.e("TAG", "sendMessage Error: ${e.message}++///+++ ${e.cause}")

                e.printStackTrace()
                return@withContext false
            }

            true
        }
    }
}