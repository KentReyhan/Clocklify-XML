package com.example.dao.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "activity_table")
data class Activity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "timer") val timer: String?,
    @ColumnInfo(name = "start_time") val startTime: Date?,
    @ColumnInfo(name = "end_time") val endTime: Date?,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "latitude") val latitude: Double?,
    @ColumnInfo(name = "activities_detail") val activitiesDetail: String?,
    )