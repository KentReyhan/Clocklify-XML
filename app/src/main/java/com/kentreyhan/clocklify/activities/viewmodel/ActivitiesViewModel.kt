package com.kentreyhan.clocklify.activities.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dao.database.ActivityDatabase
import com.example.dao.model.Activity
import com.kentreyhan.clocklify.activities.model.GroupedActivitiesModel
import com.kentreyhan.commons.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class ActivitiesViewModel : ViewModel() {

    private lateinit var db: ActivityDatabase

    private val groupedMap: MutableLiveData<HashMap<Date, ArrayList<Activity>>> =
        MutableLiveData<HashMap<Date, ArrayList<Activity>>>(HashMap())

    private val groupedPair: MutableLiveData<ArrayList<Pair<Date,Activity>>> =
        MutableLiveData<ArrayList<Pair<Date,Activity>>>(ArrayList())

    val activitiesList: MutableLiveData<ArrayList<Activity>> =
        MutableLiveData<ArrayList<Activity>>()

    val coordinate: MutableLiveData<ArrayList<Double>> =
        MutableLiveData<ArrayList<Double>>()

    val searchKeyword: MutableLiveData<String?> = MutableLiveData<String?>()
    val sortByValue: MutableLiveData<String> = MutableLiveData<String>("Latest Date")

    fun initActivitiesList(context: Context) {
        db = ActivityDatabase.getDatabase(context)
        val result = CoroutineScope(Dispatchers.IO).async {
            ArrayList(db.activityDao().getAllActivity())
        }
        runBlocking {
            activitiesList.value = result.await()
        }

        sortByDate()
        sortByCoordinates()
    }

    fun getGroupedList(): ArrayList<GroupedActivitiesModel> {
        val list: ArrayList<GroupedActivitiesModel> = arrayListOf()
        var id: Int = 1
        if(sortByValue.value=="Nearby"){
            groupedPair.value?.forEach{ group ->
                list.add(GroupedActivitiesModel(id,group.first, arrayListOf(group.second)))
                id++
            }
        }
        else if(sortByValue.value=="Latest Date"){
            groupedMap.value?.forEach { group ->
                list.add(GroupedActivitiesModel(id,group.key, group.value))
                id++
            }
        }
        return list
    }

    fun sortByDate() {
        groupedMap.value?.clear()

        val queriedActivitiesList: ArrayList<Activity> = getQueriedActivitiesList()

        for (activities in queriedActivitiesList) {
            val dateString: String = DateUtils().getFormattedDate(activities.startTime)
            val date: Date = DateUtils().parseFormattedDate(dateString)
            if (groupedMap.value?.containsKey(date) == false) {
                groupedMap.value?.put(date, arrayListOf())
            }
            groupedMap.value?.get(date)?.add(activities)
        }
        groupedMap.value?.toSortedMap()

    }

    fun sortByCoordinates() {
        if(coordinate.value.isNullOrEmpty()){
            return
        }

        groupedPair.value?.clear()

        val queriedActivitiesList: ArrayList<Activity> = getQueriedActivitiesList()

        queriedActivitiesList.sortBy {
            distance(coordinate.value!![0], coordinate.value!![1], it.latitude, it.longitude)
        }

        for (activities in queriedActivitiesList) {
            val dateString: String = DateUtils().getFormattedDate(activities.startTime)
            val date: Date = DateUtils().parseFormattedDate(dateString)
            groupedPair.value?.add(Pair(date, activities))
        }
    }

    private fun getQueriedActivitiesList(): ArrayList<Activity> {
        val queriedActivitiesList: ArrayList<Activity> = arrayListOf()
        if(searchKeyword.value != null){
            for (activities in activitiesList.value!!){
               if(activities.activitiesDetail.contains(searchKeyword.value!!)){
                   queriedActivitiesList.add(activities)
               }
            }
        }
        else {
            for (activities in activitiesList.value!!){
                queriedActivitiesList.add(activities)
            }
        }
        return queriedActivitiesList
    }

    fun onActivitiesSearchChanged(value: String) {
        searchKeyword.value = value
    }

    fun onDropdownValueChanged(value: String) {
        sortByValue.value = value
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