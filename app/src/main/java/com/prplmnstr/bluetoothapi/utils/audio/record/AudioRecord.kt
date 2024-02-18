package com.prplmnstr.bluetoothapi.utils.audio.record

import java.io.File

interface AudioRecord {

    suspend fun start(outputFile: File)
    suspend fun stop()
}