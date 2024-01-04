package com.kentreyhan.clocklify.activities.model

import java.util.Date

data class GroupedActivitiesModel(
    val id: Int,
    val date: Date,
    val activitiesList: ArrayList<ActivitiesModel>
)
