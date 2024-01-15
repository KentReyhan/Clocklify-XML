package com.example.network.dto.activity.request

import com.google.gson.annotations.SerializedName

data class ActivityRequest(
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

