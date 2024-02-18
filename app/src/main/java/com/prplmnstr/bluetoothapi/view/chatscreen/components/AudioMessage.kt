package com.prplmnstr.bluetoothapi.view.chatscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prplmnstr.bluetoothapi.R
import com.prplmnstr.bluetoothapi.model.bluetooth.BluetoothMessage
import com.prplmnstr.bluetoothapi.ui.theme.BlueViolet3
import com.prplmnstr.bluetoothapi.ui.theme.LightRed
import kotlinx.coroutines.delay


@Composable
fun AudioMessage(
    message: BluetoothMessage.AudioMessage,
    modifier: Modifier = Modifier,
    startPlaying: () -> Unit,
    stopPlaying: () -> Unit,
    seekTo: (position: Int) -> Unit,
) {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }


    LaunchedEffect(Unit) {
        //    val duration = viewModel.getAudioDuration()
        while (isPlaying) {
            delay(100) // Update progress every 100 milliseconds
            // progress = viewModel.getCurrentPosition() * 100f / duration
        }
    }

    Column(

        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = if (message.isFromLocalUser) 15.dp else 0.dp,
                    topEnd = 15.dp,
                    bottomStart = 15.dp,
                    bottomEnd = if (message.isFromLocalUser) 0.dp else 15.dp
                )
            )
            .background(
                if (message.isFromLocalUser) BlueViolet3 else LightRed
            )
            .padding(16.dp),
    ) {


        Row(

            verticalAlignment = Alignment.CenterVertically

        ) {
            // Play button
            IconButton(
                onClick = {
                    if (isPlaying) {
                        stopPlaying()
                    } else {
                        startPlaying()
                    }
                    isPlaying = !isPlaying

                },

                content = {
                    Icon(

                        painterResource(
                            id =
                            if (isPlaying) R.drawable.pause_24 else R.drawable.play_arrow_24
                        ),
                        contentDescription = "Play/Pause",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp),
                    )
                }
            )

            // Slider for seeking
            Slider(
                value = progress,
                onValueChange = {
                    progress = it
                    seekTo(it.toInt())
                },
                valueRange = 0f..100f,
                onValueChangeFinished = {
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            )
            Text(
                text = "11:00 AM",
                fontSize = 10.sp,
                color = Color.Black,

                )
        }

    }
}


