package com.prplmnstr.bluetoothchat.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prplmnstr.bluetoothchat.view.chatscreen.ChatScreen
import com.prplmnstr.bluetoothchat.view.devicescreen.DeviceScreen
import com.prplmnstr.bluetoothchat.view.newsscreen.NewsScreen
import com.prplmnstr.bluetoothchat.view.splashscreen.SplashScreen
import com.prplmnstr.bluetoothchat.viewmodel.BluetoothViewModel

@Composable
fun App(
    enableDiscovery:()->Unit
) {
    val context = LocalContext.current
    Log.e("TAG", "App: running")
    val navController = rememberNavController()
    val viewModel = hiltViewModel<BluetoothViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.errorMessage) {
        state.errorMessage?.let { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(key1 = state.isConnected) {
        if (state.isConnected) {
            Toast.makeText(
                context,
                "You're connected!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    NavHost(
        navController = navController, startDestination = Routes.SPLASH_SCREEN
    ) {
        composable(
            Routes.CHAT_SCREEN
        ) {
            /* Directly transferring viewmodel to the composible is not recommended,
            not a best practice according to official documentation. so passed only required
            functions to the screens.

             */
            ChatScreen(
                navController, state,
                viewModel::sendMessage,
                viewModel::sendMessage,
                viewModel::connectToDevice,
                viewModel::disconnectFromDevice,
                startRecording = viewModel::startRecord,
                stopRecording = viewModel::stopRecord,
                createAudioFile = viewModel::createAudioFile,
                startPlaying = viewModel::startPlay,
                stopPlaying = viewModel::stopPlay,
                seekTo = viewModel::seekTo,
            )
        }
        composable(Routes.DEVICE_SCREEN) {
            DeviceScreen(
                navController,
                state,
                viewModel::startScan,
                viewModel::stopScan,
                viewModel::waitForIncomingConnections,
                viewModel::connectToDevice
            )
        }
        composable(Routes.SPLASH_SCREEN) {
            SplashScreen(navController,enableDiscovery)
        }
        composable(Routes.NEWS_SCREEN) {
            NewsScreen()
        }
    }
}


object Routes {
    const val CHAT_SCREEN = "chatScreen"
    const val SPLASH_SCREEN = "splashScreen"
    const val DEVICE_SCREEN = "deviceScreen"
    const val NEWS_SCREEN = "newsScreen"
}