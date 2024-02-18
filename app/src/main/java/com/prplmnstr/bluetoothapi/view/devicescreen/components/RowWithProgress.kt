package com.prplmnstr.bluetoothapi.view.devicescreen.components

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RowWithProgress(
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    modifier: Modifier = Modifier,
    initialProgressVisible: Boolean = false
) {
    var progressVisible by remember { mutableStateOf(initialProgressVisible) }
    var buttonText by remember { mutableStateOf("Start") }
    val context = LocalContext.current
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Text(
            text = "New Devices",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)

        )
        if (progressVisible) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )
        }
        IconButton(
            onClick = {
                if (!progressVisible) {
                    onStartScan()
                    Toast.makeText(context, "Scanning started", Toast.LENGTH_SHORT).show()
                } else {
                    onStopScan()
                }

                progressVisible = !progressVisible
                buttonText = if (progressVisible) "Stop" else "Start"

            }
        ) {
            Icon(

                imageVector = if (progressVisible) Icons.Filled.Close else Icons.Filled.Refresh,
                contentDescription = "Toggle Progress",

                )
        }

    }
}

@Preview
@Composable
fun PreviewRowWithProgress() {
    RowWithProgress({}, {})
}