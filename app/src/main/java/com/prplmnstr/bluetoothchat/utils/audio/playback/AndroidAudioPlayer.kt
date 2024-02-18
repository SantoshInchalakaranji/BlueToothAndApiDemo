package com.prplmnstr.bluetoothchat.utils.audio.playback

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
) : AudioPlayer {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            start()
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }

    override fun seekTo(position: Int) {
        val duration = player?.duration?.toFloat()
        val seekPosition = (duration!! * position / 100f).toInt()
        try {
            player?.seekTo(seekPosition)
        } catch (e: Exception) {
            Log.e("TAG", "seekTo: $e")
        }
    }
}