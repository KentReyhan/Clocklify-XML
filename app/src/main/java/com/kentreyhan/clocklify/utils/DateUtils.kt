package com.kentreyhan.clocklify.utils

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DateUtils {
    private val localDateFormatter = SimpleDateFormat("d MMM yyyy", Locale.US)
    private val localTimeFormatter = SimpleDateFormat("HH:mm:ss", Locale.US)

    fun getFormattedCurrentDate(date: Date): String {
        return localDateFormatter.format(date)
    }

    fun getFormattedCurrentTime(date: Date): String {
        return localTimeFormatter.format(date)
    }
}