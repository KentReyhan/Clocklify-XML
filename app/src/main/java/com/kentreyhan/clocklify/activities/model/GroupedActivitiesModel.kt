package com.kentreyhan.clocklify.activities.model

import com.example.dao.model.Activity
import java.util.Date

data class GroupedActivitiesModel(
    val id: Int,
    val date: Date,
    val activitiesList: ArrayList<Activity>
)
