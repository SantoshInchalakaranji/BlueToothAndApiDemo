package com.prplmnstr.bluetoothchat.view.splashscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prplmnstr.bluetoothchat.R
import com.prplmnstr.bluetoothchat.view.Routes
import com.prplmnstr.bluetoothchat.view.splashscreen.components.CardWithImage

@Composable
fun SplashScreen(
    navController: NavController,
    enableDiscovery:()->Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        CardWithImage(
            imageResource = painterResource(id = R.drawable.bluetooth),
            text = "Bluetooth Demo",
            onClick = {
                enableDiscovery()
                navController.navigate(
                    route = Routes.DEVICE_SCREEN
                )
            }

        )
        CardWithImage(
            imageResource = painterResource(id = R.drawable.api),
            text = "News Api Demo",
            onClick = {

                navController.navigate(
                    route = Routes.NEWS_SCREEN
                )
            }

        )
    }

}