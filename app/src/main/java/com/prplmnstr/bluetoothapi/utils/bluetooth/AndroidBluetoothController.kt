package com.prplmnstr.bluetoothapi.utils.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.prplmnstr.bluetoothapi.model.bluetooth.BluetoothDeviceDomain
import com.prplmnstr.bluetoothapi.model.bluetooth.BluetoothMessage
import com.prplmnstr.bluetoothapi.utils.DateAndTime
import com.prplmnstr.bluetoothapi.utils.bluetooth.helper.toBluetoothDeviceDomain
import com.prplmnstr.bluetoothapi.utils.bluetooth.helper.toByteArray
import com.prplmnstr.bluetoothapi.utils.bluetooth.receiver.BluetoothStateReceiver
import com.prplmnstr.bluetoothapi.utils.bluetooth.receiver.FoundDeviceReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.UUID


/**
 * Implementation of [BluetoothController] for Android devices.
 *
 * This class provides functionality to manage Bluetooth connections,
 * discover devices, and send/receive messages.
 * It requires the necessary Bluetooth permissions to perform these operations.
 *
 * @property context The application context.
 * @property bluetoothManager The Bluetooth manager responsible for managing Bluetooth operations.
 * @property bluetoothAdapter The Bluetooth adapter for the device.
 * @property dataTransferService The service for transferring data over Bluetooth.
 * @property _isConnected A mutable state flow representing the connection status.
 * @property _connectedDevice A mutable state flow representing the connected Bluetooth device.
 * @property _scannedDevices A mutable state flow representing the list of scanned Bluetooth devices.
 * @property _pairedDevices A mutable state flow representing the list of paired Bluetooth devices.
 * @property _errors A mutable shared flow representing error messages.
 * @property foundDeviceReceiver The broadcast receiver for handling discovered Bluetooth devices.
 * @property bluetoothStateReceiver The broadcast receiver for handling Bluetooth state changes.
 * @property currentServerSocket The current Bluetooth server socket.
 * @property currentClientSocket The current Bluetooth client socket.
 */




@SuppressLint("MissingPermission")
class AndroidBluetoothController(
    private val context: Context
) : BluetoothController {


    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private var dataTransferService: BluetoothDataTransferService? = null

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()


    private val _connectedDevice = MutableStateFlow(BluetoothDeviceDomain("", ""))
    override val connectedDevice: StateFlow<BluetoothDeviceDomain>
        get() = _connectedDevice.asStateFlow()

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()

    private val _errors = MutableSharedFlow<String>()
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()


    private val foundDeviceReceiver = FoundDeviceReceiver { device ->

        _scannedDevices.update { devices ->
            Log.d("TAG", "scanner : yes ")
            val newDevice = device.toBluetoothDeviceDomain()

            if (newDevice in devices) devices else devices + newDevice

        }


    }


    private val bluetoothStateReceiver = BluetoothStateReceiver { isConnected, bluetoothDevice ->
        if (bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == true) {
            _isConnected.update { isConnected }
            _connectedDevice.update { bluetoothDevice.toBluetoothDeviceDomain() }

        } else {
            CoroutineScope(Dispatchers.IO).launch {
                _errors.emit("Can't connect to a non-paired device.")
            }
        }
    }

    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null

    init {
        Log.e("TAG", "android controller: running")
        updatePairedDevices()
        Log.e("TAG", "Registered  : bluetoothStateReceiver ")
        context.registerReceiver(

            bluetoothStateReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
        )
    }


    override fun startDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)
        ) {
            Toast.makeText(context, "Please Enable Bluetooth Permissions. ", Toast.LENGTH_SHORT)
                .show()
            Log.e("TAG", "scanner : No ")
            return
        }
        Log.e("TAG", "Registered  : foundDeviceReceiver ")

        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )

        updatePairedDevices()
        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            Toast.makeText(context, "Please Enable Bluetooth Permissions. ", Toast.LENGTH_SHORT)
                .show()
            return
        }

        bluetoothAdapter?.cancelDiscovery()
    }

    override fun startBluetoothServer(): Flow<ConnectionResult> {
        return flow {
            if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                Toast.makeText(context, "Please Enable Bluetooth Permissions. ", Toast.LENGTH_SHORT)
                    .show()
            }

            currentServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                "chat_service",
                UUID.fromString(SERVICE_UUID)
            )

            var shouldLoop = true
            while (shouldLoop) {
                currentClientSocket = try {
                    currentServerSocket?.accept()
                } catch (e: IOException) {
                    shouldLoop = false
                    null
                }
                emit(ConnectionResult.ConnectionEstablished(_connectedDevice.value))
                currentClientSocket?.let {
                    currentServerSocket?.close()
                    val service = BluetoothDataTransferService(it)
                    dataTransferService = service
                    try{
                        emitAll(
                            service
                                .listenForIncomingMessages()
                                .map {
                                    ConnectionResult.TransferSucceeded(it)
                                }
                        )
                    }catch (e:IOException){
                        Log.e("TAG", "startBluetoothServer: ${e.toString()}", )
                    }


                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }


    override fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult> {
        return flow {
            if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                Toast.makeText(context, "Please Enable Bluetooth Permissions. ", Toast.LENGTH_SHORT)
                    .show()
                // throw SecurityException("No BLUETOOTH_CONNECT permission")
            }

            currentClientSocket = bluetoothAdapter
                ?.getRemoteDevice(device.address)
                ?.createRfcommSocketToServiceRecord(
                    UUID.fromString(SERVICE_UUID)
                )
            stopDiscovery()

            currentClientSocket?.let { socket ->
                try {
                    socket.connect()
                    emit(ConnectionResult.ConnectionEstablished(_connectedDevice.value))

                    BluetoothDataTransferService(socket).also {
                        dataTransferService = it
                        emitAll(
                            it.listenForIncomingMessages()

                                .map { bluetoothMessage ->

                                    ConnectionResult.TransferSucceeded(bluetoothMessage)
                                }
                        )
                    }
                } catch (e: IOException) {
                    Log.d("TAG", "connectToDevice: ${e.message}+++++ ${e.cause}")
                    socket.close()
                    currentClientSocket = null
                    emit(ConnectionResult.Error("Connection was interrupted"))
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    @SuppressLint("HardwareIds")
    override suspend fun trySendMessage(message: String): BluetoothMessage? {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            Toast.makeText(context, "Please Enable Bluetooth Permissions. ", Toast.LENGTH_SHORT)
                .show()
            return null
        }

        if (dataTransferService == null) {
            return null
        }

        val bluetoothMessage = BluetoothMessage.TextMessage(
            text = message,
            senderName = bluetoothAdapter?.name ?: "Unknown name",
            senderAddress = "",
            date = DateAndTime.getTodayDate(),
            time = DateAndTime.getCurrentTime(),
            isFromLocalUser = true
        )

        dataTransferService?.sendMessage(bluetoothMessage.toByteArray())
        bluetoothMessage.senderAddress = _connectedDevice.value.address

        return bluetoothMessage
    }

    @SuppressLint("HardwareIds")
    override suspend fun trySendMessage(audioData: File): BluetoothMessage? {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            Toast.makeText(context, "Please Enable Bluetooth Permissions. ", Toast.LENGTH_SHORT)
                .show()
            return null
        }

        Log.e("TAG", "trySendMessage  : came")
        if (dataTransferService == null) {
            return null
        }

        val bluetoothMessage = BluetoothMessage.AudioMessage(
            audioData = audioData.readBytes(),
            senderName = bluetoothAdapter?.name ?: "Unknown name",
            senderAddress = "",
            date = DateAndTime.getTodayDate(),
            time = DateAndTime.getCurrentTime(),
            isFromLocalUser = true
        )

        //  dataTransferService?.sendMessage(bluetoothMessage.toByteArray())

        Log.e("TAG", "dataTransferService  : came")
        bluetoothMessage.senderAddress = _connectedDevice.value.address
        return bluetoothMessage
    }

    override fun closeConnection() {
        currentClientSocket?.close()
        currentServerSocket?.close()
        currentClientSocket = null
        currentServerSocket = null
        //  release()
    }

    override fun release() {
        try {
            context.unregisterReceiver(foundDeviceReceiver)
            context.unregisterReceiver(bluetoothStateReceiver)
            closeConnection()
        } catch (e: Exception) {
            Log.e("TAG", "release: $e")
        }

    }


    private fun updatePairedDevices() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            Toast.makeText(context, "Please Enable Bluetooth Permissions. ", Toast.LENGTH_SHORT)
                .show()
            return
        }
        bluetoothAdapter
            ?.bondedDevices
            ?.filter {
                it.bluetoothClass.deviceClass == BluetoothClass.Device.PHONE_SMART
            }
            ?.map {
                it.toBluetoothDeviceDomain()
            }
            ?.also { devices ->
                _pairedDevices.update { devices }
            }
    }

    private fun hasPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }

    }

    companion object {
        const val SERVICE_UUID = "27b7d1da-08c7-4505-a6d1-2459987e5e2d"
    }
}