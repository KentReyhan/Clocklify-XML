package com.example.network.dto.activity.response

import android.icu.text.SimpleDateFormat
import com.example.dao.model.Activity
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.Locale

data class ActivityResponse(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("datestart")
    var datestart: String? = null,

    @SerializedName("dateend")
    var dateend: String? = null,

    @SerializedName("activity")
    var activity: String? = null,

    @SerializedName("duration")
    var duration: String? = null,

    @SerializedName("timestart")
    var timestart: String? = null,

    @SerializedName("timeend")
    var timeend: String? = null,

    @SerializedName("locationlong")
    var locationlong: Double? = null,

    @SerializedName("locationlat")
    var locationlat: Double? = null
)

fun parseFromJsonDateTime(date: String): Date {
    val fromJsonDateTimeFormatter = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.US)
    return fromJsonDateTimeFormatter.parse(date)
}

fun ActivityResponse.toModel(): Activity {
    val formattedStartTime = parseFromJsonDateTime("$datestart $timestart")
    val formattedEndTime =parseFromJsonDateTime("$dateend $timeend")
    return Activity(id, duration, formattedStartTime, formattedEndTime, locationlat, locationlong, activity)
}
