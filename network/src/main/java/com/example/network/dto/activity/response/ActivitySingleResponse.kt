package com.example.network.dto.activity.response

import com.example.dao.model.Activity
import com.google.gson.annotations.SerializedName

data class ActivitySingleResponse(
    @SerializedName("activity")
    var singleActivity: ActivityResponse
)

fun ActivitySingleResponse.toModel(): Activity {
    return singleActivity.toModel()
}