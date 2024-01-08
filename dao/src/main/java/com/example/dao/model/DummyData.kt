package com.example.dao.model

import com.kentreyhan.commons.utils.DateUtils

object DummyData{
    val dummyActivities: Activity =
        Activity(
            id = 1,
            timer = "00 : 30 : 01",
            activitiesDetail = "Treadmill",
            startTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:00:01"),
            endTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:30:02"),
            latitude = 12.915555,
            longitude = 77.21146,
        )

    val dummyActivities2: Activity =
        Activity(
            id = 2,
            timer = "01 : 30 : 01",
            activitiesDetail = "Running",
            startTime = DateUtils().parseFormattedDateTime("13 Dec 2023 11:00:01"),
            endTime = DateUtils().parseFormattedDateTime("13 Dec 2023 12:30:02"),
            latitude = 09.915543,
            longitude = 77.21157,
        )

   val dummyActivities3: Activity =
        Activity(
            id = 3,
            timer = "01 : 30 : 01",
            activitiesDetail = "Running",
            startTime = DateUtils().parseFormattedDateTime("13 Dec 2023 11:00:01"),
            endTime = DateUtils().parseFormattedDateTime("13 Dec 2023 12:30:02"),
            latitude = 09.915543,
            longitude = 77.21157,
        )

    val dummyActivities4: Activity =
        Activity(
            id = 4,
            timer = "00 : 30 : 01",
            activitiesDetail = "Treadmill",
            startTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:00:01"),
            endTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:30:02"),
            latitude = 12.915555,
            longitude = 77.21146,
        )

    val dummyActivities5: Activity =
        Activity(
            id = 5,
            timer = "00 : 30 : 01",
            activitiesDetail = "Treadmill",
            startTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:00:01"),
            endTime = DateUtils().parseFormattedDateTime("12 Dec 2023 12:30:02"),
            latitude = 12.915555,
            longitude = 77.21146,
        )
}