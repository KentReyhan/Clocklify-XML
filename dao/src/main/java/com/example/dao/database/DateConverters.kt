package com.example.dao.database;

import androidx.room.TypeConverter
import java.util.Date

class DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}
