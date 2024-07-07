package ru.netology.neworkapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object ConvertDateTime {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedCurrentDateTime(): String {
        val currentDateTime = OffsetDateTime.now()
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return currentDateTime.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val calendarInputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")
    @RequiresApi(Build.VERSION_CODES.O)
    private val outputISOFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedISODateTime(inputDateTime: String): String {
        val localDateTime = LocalDateTime.parse(inputDateTime, calendarInputFormatter)
        val formatter = localDateTime.atOffset(ZoneOffset.UTC)
        return formatter.format(outputISOFormatter)
    }
}