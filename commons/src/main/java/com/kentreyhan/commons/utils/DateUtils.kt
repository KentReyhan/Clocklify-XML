package com.kentreyhan.commons.utils

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DateUtils {

    fun getFormattedDate(date: Date): String {
        return localDateFormatter.format(date)
    }

    fun getFormattedTime(date: Date): String {
        return localTimeFormatter.format(date)
    }

    fun parseFormattedTime(date: String): Date {
        return localTimeFormatter.parse(date)
    }

    fun parseFormattedDate(date: String): Date {
        return localDateFormatter.parse(date)
    }

    fun getFormattedRequestedDate(date: Date): String {
        return requestedDateFormatter.format(date)
    }

    fun getFormattedRequestedTime(date: Date): String {
        return requestedTimeFormatter.format(date)
    }

    companion object {
        private val localDateFormatter = SimpleDateFormat("d MMM yyyy", Locale.US)
        private val localTimeFormatter = SimpleDateFormat("HH:mm:ss", Locale.US)

        private val requestedDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private val requestedTimeFormatter = SimpleDateFormat("HH:mm:ss", Locale.US)

        private val localDateTimeFormatter = SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.US)
    }
}