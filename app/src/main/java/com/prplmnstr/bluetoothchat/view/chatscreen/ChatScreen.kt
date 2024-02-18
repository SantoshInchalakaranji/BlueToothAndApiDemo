package com.prplmnstr.bluetoothchat.view.chatscreen

import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prplmnstr.bluetoothchat.R
import com.prplmnstr.bluetoothchat.model.bluetooth.BluetoothDeviceDomain
import com.prplmnstr.bluetoothchat.model.bluetooth.BluetoothMessage
import com.prplmnstr.bluetoothchat.model.bluetooth.BluetoothUiState
import com.prplmnstr.bluetoothchat.view.chatscreen.components.AudioMessage
import com.prplmnstr.bluetoothchat.view.chatscreen.components.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(

    navController: NavController,
    state: BluetoothUiState,
    sendTextMessage: (String) -> Unit,
    sendAudioMessage: () -> Unit,
    connectToDevice: (BluetoothDeviceDomain) -> Unit,
    disconnectFromDevice: () -> Unit,
    startRecording: KSuspendFunction0<Unit>,

    stopRecording: KSuspendFunction0<Unit>,
    createAudioFile: (name: String) -> Unit,
    startPlaying: () -> Unit,
    stopPlaying: () -> Unit,
    seekTo: (position: Int) -> Unit,

    ) {

    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }

    val message = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.peerDevice?.name ?: "No Name",
                modifier = Modifier.weight(1f)
            )
            if (state.isConnecting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )

            } else if (state.isConnected) {
                IconButton(onClick = {
                     disconnectFromDevice()
                }

                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Disconnect"
                    )
                }
            } else {
                Button(onClick = { state.peerDevice?.let { connectToDevice(it) } }) {
                    Text(text = "Connect")
                }
            }

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.messages) { message ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    when (message) {
                        is BluetoothMessage.TextMessage -> {
                            ChatMessage(
                                message = message,
                                modifier = Modifier
                                    .align(
                                        if (message.isFromLocalUser) Alignment.End else Alignment.Start
                                    )
                            )
                        }

                        is BluetoothMessage.AudioMessage -> {
                            AudioMessage(
                                message = message,
                                modifier = Modifier
                                    .align(
                                        if (message.isFromLocalUser) Alignment.End else Alignment.Start
                                    ),

                                startPlaying,
                                stopPlaying,
                                seekTo
                            )
                        }

                        is BluetoothMessage.ImageMessage -> {

                        }
                    }

                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            AnimatedVisibility(
                visible = isRecording,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text("Recording...") },
                    leadingIcon = {
                        Icon(
                            painterResource(
                                id = R.drawable.mic_24
                            ), contentDescription = ""
                        )
                    },

                    )
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = message.value,
                onValueChange = { message.value = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(text = "Message")
                }
            )
            if (state.isConnected) {
                ButtonWithRecording(
                    isRecording,
                    onRecordingChanged = { isRecording = it },
                    sendAudioMessage = sendAudioMessage,
                    startRecording,
                    stopRecording,
                    createAudioFile
                )
            }
            IconButton(onClick = {
                if (state.isConnected) {
                    sendTextMessage(message.value)
                    message.value = ""
                    keyboardController?.hide()
                } else {
                    Toast.makeText(
                        context,
                        "Please connect before sending message.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send message"
                )
            }
        }
    }
    BackHandler {
        disconnectFromDevice()
        navController.navigateUp()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ButtonWithRecording(
    isRecording: Boolean, onRecordingChanged: (Boolean) -> Unit,
    sendAudioMessage: () -> Unit,
    startRecording: KSuspendFunction0<Unit>,
    stopRecording: KSuspendFunction0<Unit>,
    createAudioFile: (name: String) -> Unit,
) {


    val transition = updateTransition(targetState = isRecording)
    val coroutineScope = rememberCoroutineScope()

    val scale by transition.animateFloat(
        transitionSpec = {
            if (targetState) {
                tween(durationMillis = 300)
            } else {
                tween(durationMillis = 300)
            }
        }
    ) { recordingState ->
        if (recordingState) 2f else 1f
    }


    IconButton(
        onClick = { /* No-op, as the touch events handle recording */ },
        modifier = Modifier
            .padding(8.dp)
            .pointerInteropFilter { event ->
                when {
                    event.action == MotionEvent.ACTION_DOWN -> {

                        onRecordingChanged(true)
                        coroutineScope.launch(Dispatchers.IO) {
                            createAudioFile("new_recording")
                            delay(500)
                            startRecording()
                        }
                    }

                    event.action == MotionEvent.ACTION_UP -> {
                        onRecordingChanged(false)
                        coroutineScope.launch(Dispatchers.IO) {
                            stopRecording()
                            sendAudioMessage()
                        }


                    }
                }
                true // Returning true to indicate the event has been consumed
            }
            .scale(scale)
    ) {
        Icon(painterResource(id = R.drawable.mic_24), contentDescription = "")
    }
}
