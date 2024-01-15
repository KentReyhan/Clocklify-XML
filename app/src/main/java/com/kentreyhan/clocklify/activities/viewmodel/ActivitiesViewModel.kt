package com.kentreyhan.clocklify.activities.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dao.database.ActivityDatabase
import com.example.dao.model.Activity
import com.example.network.api.ApiServiceBuilder
import com.example.network.dto.activity.response.ActivityListResponse
import com.example.network.dto.activity.response.toModel
import com.example.network.service.ActivityService
import com.kentreyhan.clocklify.activities.model.GroupedActivitiesModel
import com.kentreyhan.commons.utils.DateUtils
import com.kentreyhan.commons.utils.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections
import java.util.Date
import java.util.TreeMap
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class ActivitiesViewModel : ViewModel() {

    private lateinit var db: ActivityDatabase

    private val groupedMap: MutableLiveData<TreeMap<Date, ArrayList<Activity>>> =
        MutableLiveData<TreeMap<Date, ArrayList<Activity>>>(TreeMap(Collections.reverseOrder()))

    private val groupedPair: MutableLiveData<ArrayList<Pair<Date, Activity>>> =
        MutableLiveData<ArrayList<Pair<Date, Activity>>>(ArrayList())

    val activitiesList: MutableLiveData<ArrayList<Activity>> =
        MutableLiveData<ArrayList<Activity>>()

    val coordinate: MutableLiveData<ArrayList<Double>> =
        MutableLiveData<ArrayList<Double>>()

    val searchKeyword: MutableLiveData<String?> = MutableLiveData<String?>()
    val sortByValue: MutableLiveData<String> = MutableLiveData<String>("Latest Date")

    val isLoading: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()

    private lateinit var activityService: ActivityService


    fun onActivitiesSearchChanged(value: String) {
        searchKeyword.value = value
    }

    fun onDropdownValueChanged(value: String) {
        sortByValue.value = value
    }

    fun onIsLoadingChanged(value: Boolean) {
        isLoading.postValue(value)
    }

    fun fetchActivityList(context: Context) {
        activityService = ApiServiceBuilder.getActivityInstance(context)

        onIsLoadingChanged(true)
        activityService.getAllActivity(sort = "DESC").enqueue(object : Callback<ActivityListResponse> {

            override fun onFailure(call: Call<ActivityListResponse>, t: Throwable) {
                ToastUtils().showToast(context, "Fetching Activity List Failed")
                onIsLoadingChanged(false)
                return
            }

            override fun onResponse(call: Call<ActivityListResponse>, response: Response<ActivityListResponse>) {
                val activityResponse = response.body()
                if (activityResponse == null) {
                    ToastUtils().showToast(context, "Fetching Activity List Failed")
                    onIsLoadingChanged(false)
                    return
                }
                activitiesList.postValue(activityResponse.toModel())
                onIsLoadingChanged(false)
            }
        })
    }

    fun getGroupedList(): ArrayList<GroupedActivitiesModel> {
        val list: ArrayList<GroupedActivitiesModel> = arrayListOf()
        var id: Int = 1
        if (sortByValue.value == "Nearby") {
            groupedPair.value?.forEach { group ->
                list.add(GroupedActivitiesModel(id, group.first, arrayListOf(group.second)))
                id++
            }
        } else if (sortByValue.value == "Latest Date") {
            groupedMap.value?.forEach { group ->
                list.add(GroupedActivitiesModel(id, group.key, group.value))
                id++
            }
        }
        return list
    }

    fun sortByDate() {
        groupedMap.value?.clear()

        val queriedActivitiesList: ArrayList<Activity> = getQueriedActivitiesList()

        for (activities in queriedActivitiesList) {
            val dateString: String = activities.startTime?.let { DateUtils().getFormattedDate(it) }.toString()
            val date: Date = DateUtils().parseFormattedDate(dateString)
            if (groupedMap.value?.containsKey(date) == false) {
                groupedMap.value?.put(date, arrayListOf())
            }
            groupedMap.value?.get(date)?.add(activities)
        }
    }

    fun sortByCoordinates() {
        if (coordinate.value.isNullOrEmpty()) {
            return
        }

        groupedPair.value?.clear()

        val queriedActivitiesList: ArrayList<Activity> = getQueriedActivitiesList()

        queriedActivitiesList.sortBy {

            it.latitude?.let { it1 ->
                it.longitude?.let { it2 ->
                    distance(
                        coordinate.value!![0], coordinate.value!![1], it1,
                        it2
                    )
                }
            }
        }

        for (activities in queriedActivitiesList) {
            val dateString: String = activities.startTime?.let { DateUtils().getFormattedDate(it) }.toString()
            val date: Date = DateUtils().parseFormattedDate(dateString)
            groupedPair.value?.add(Pair(date, activities))
        }
    }

    private fun getQueriedActivitiesList(): ArrayList<Activity> {
        val queriedActivitiesList: ArrayList<Activity> = arrayListOf()
        if(activitiesList.value==null){
            return queriedActivitiesList
        }
        if (searchKeyword.value != null) {
            for (activities in activitiesList.value!!) {
                if (activities.activitiesDetail?.uppercase()?.contains(searchKeyword.value!!.uppercase()) == true) {
                    queriedActivitiesList.add(activities)
                }
            }
        } else {
            for (activities in activitiesList.value!!) {
                queriedActivitiesList.add(activities)
            }
        }
        return queriedActivitiesList
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist =
            (sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + (cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}