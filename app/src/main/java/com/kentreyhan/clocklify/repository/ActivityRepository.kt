package com.kentreyhan.clocklify.repository

import com.kentreyhan.clocklify.activities.model.ActivitiesModel
import com.kentreyhan.clocklify.utils.DateUtils

object ActivityRepository {

    private val dummyActivities: ActivitiesModel =
        ActivitiesModel(
            id = 1,
            timer = "00 : 30 : 01",
            activitiesDetail = "Treadmill",
            startTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:00:01"),
            endTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:30:02"),
            latitude = 12.915555,
            longitude = 77.21146,
        )

    private val dummyActivities2: ActivitiesModel =
        ActivitiesModel(
            id = 2,
            timer = "01 : 30 : 01",
            activitiesDetail = "Running",
            startTime = DateUtils().parseFormattedDateTime("13 Dec 2023 11:00:01"),
            endTime = DateUtils().parseFormattedDateTime("13 Dec 2023 12:30:02"),
            latitude = 09.915543,
            longitude = 77.21157,
        )

    private val dummyActivities3: ActivitiesModel =
        ActivitiesModel(
            id = 3,
            timer = "01 : 30 : 01",
            activitiesDetail = "Running",
            startTime = DateUtils().parseFormattedDateTime("13 Dec 2023 11:00:01"),
            endTime = DateUtils().parseFormattedDateTime("13 Dec 2023 12:30:02"),
            latitude = 09.915543,
            longitude = 77.21157,
        )

    private val dummyActivities4: ActivitiesModel =
        ActivitiesModel(
            id = 4,
            timer = "00 : 30 : 01",
            activitiesDetail = "Treadmill",
            startTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:00:01"),
            endTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:30:02"),
            latitude = 12.915555,
            longitude = 77.21146,
        )

    private val dummyActivities5: ActivitiesModel =
        ActivitiesModel(
            id = 5,
            timer = "00 : 30 : 01",
            activitiesDetail = "Treadmill",
            startTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:00:01"),
            endTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:30:02"),
            latitude = 12.915555,
            longitude = 77.21146,
        )

    private val list = arrayListOf(dummyActivities, dummyActivities2, dummyActivities3, dummyActivities4, dummyActivities5)

    fun addItems(vararg items: ActivitiesModel) {
        list.addAll(items)
    }

    fun addItem(item: ActivitiesModel) {
        list.add(item)
    }

    fun updateDetailItem(id: Int,value: String) {
        list.find { it.id == id }?.activitiesDetail = value
    }

    fun removeAll() {
        list.clear()
    }

    fun getSize(): Int {
        return list.size
    }
    fun getId(): Int {
        return list.last().id
    }

    fun removeItemById(id: Int) = list.removeIf { it.id == id }
    fun removeItemByPosition(position:Int) = list.removeAt(position)

    fun getChannels(): ArrayList<ActivitiesModel> = list
}