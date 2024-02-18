package com.prplmnstr.bluetoothchat.utils


import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import java.util.Date
import java.util.Locale

class DateAndTime {
    companion object {

        fun getTodayDate(): String {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                val today = Date()
                val formatter = SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
                val formattedDate = formatter.format(today)

                return formattedDate
            } else {
                val today = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.ENGLISH)
                val formattedDate = today.format(formatter)

                return formattedDate
            }
        }

        fun getCurrentTime(): String {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                val currentTime = Date()
                val timeFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                val formattedTime = timeFormatter.format(currentTime)
                return formattedTime
            } else {
                val today = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
                val formattedDate = today.format(formatter)

                return formattedDate
            }

        }

    }
}