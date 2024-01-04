package com.kentreyhan.clocklify.activities.model

import java.io.Serializable
import java.util.Date
data class ActivitiesModel (
    val id: Int,
    val timer: String,
    val startTime: Date,
    val endTime: Date,
    val longitude: Double,
    val latitude: Double,
    var activitiesDetail: String,
) : Serializable
