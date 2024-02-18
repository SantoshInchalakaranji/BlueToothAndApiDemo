package com.prplmnstr.bluetoothchat.utils.audio.record

import java.io.File

interface AudioRecord {

    suspend fun start(outputFile: File)
    suspend fun stop()
}