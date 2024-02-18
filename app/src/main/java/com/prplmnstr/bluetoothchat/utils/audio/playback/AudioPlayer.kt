package com.prplmnstr.bluetoothchat.utils.audio.playback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
    fun seekTo(position: Int)
}