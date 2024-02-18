package com.prplmnstr.bluetoothapi.view.chatscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prplmnstr.bluetoothapi.model.bluetooth.BluetoothMessage
import com.prplmnstr.bluetoothapi.ui.theme.BlueToothChatTheme
import com.prplmnstr.bluetoothapi.ui.theme.BlueViolet3
import com.prplmnstr.bluetoothapi.ui.theme.LightRed


@Composable
fun ChatMessage(
    message: BluetoothMessage.TextMessage,
    modifier: Modifier = Modifier
) {
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
            .padding(16.dp)
    ) {

        Text(
            text = message.text,
            color = Color.Black,
            modifier = Modifier.widthIn(max = 250.dp)
        )
        Text(
            text = message.time,
            fontSize = 10.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Preview
@Composable
fun ChatMessagePreview() {
    BlueToothChatTheme {
        ChatMessage(
            message = BluetoothMessage.TextMessage(
                text = "Hello World! How are you I am not well hope uou doing well",
                senderName = "Pixel 6",
                senderAddress = "address",
                date = "11, Jan",
                time = "11:00 AM",
                isFromLocalUser = false
            )
        )
    }


}