package com.example.network.dto.activity.response

import com.example.dao.model.Activity
import com.google.gson.annotations.SerializedName

data class ActivityListResponse(
    @SerializedName("activities")
    var activities: ArrayList<ActivityResponse>
)

fun ActivityListResponse.toModel(): ArrayList<Activity> {
    val activityList = arrayListOf<Activity>()
    for(activity in activities){
        activityList.add(activity.toModel())
    }
    return activityList
}
