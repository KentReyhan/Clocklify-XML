package com.kentreyhan.clocklify.activities.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kentreyhan.clocklify.activities.model.ActivitiesModel
import com.kentreyhan.clocklify.activities.model.GroupedActivitiesModel
import com.kentreyhan.clocklify.repository.ActivityRepository
import com.kentreyhan.clocklify.utils.DateUtils
import java.util.Date
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class ActivitiesViewModel : ViewModel() {

    private val groupedMap: MutableLiveData<HashMap<Date, ArrayList<ActivitiesModel>>> =
        MutableLiveData<HashMap<Date, ArrayList<ActivitiesModel>>>(HashMap())

    private val groupedPair: MutableLiveData<ArrayList<Pair<Date,ActivitiesModel>>> =
        MutableLiveData<ArrayList<Pair<Date,ActivitiesModel>>>(ArrayList())

    val activitiesList: MutableLiveData<ArrayList<ActivitiesModel>> =
        MutableLiveData<ArrayList<ActivitiesModel>>()

    val coordinate: MutableLiveData<ArrayList<Double>> =
        MutableLiveData<ArrayList<Double>>()

    val searchKeyword: MutableLiveData<String?> = MutableLiveData<String?>()
    val sortByValue: MutableLiveData<String> = MutableLiveData<String>("Latest Date")

    fun initActivitiesList() {
        //TODO: Implement SQLite for save data, import activities from there
        activitiesList.value = ActivityRepository.getChannels()
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

        val queriedActivitiesList: ArrayList<ActivitiesModel> = getQueriedActivitiesList()

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

        val queriedActivitiesList: ArrayList<ActivitiesModel> = getQueriedActivitiesList()

        queriedActivitiesList.sortBy {
            distance(coordinate.value!![0], coordinate.value!![1], it.latitude, it.longitude)
        }

        for (activities in queriedActivitiesList) {
            val dateString: String = DateUtils().getFormattedDate(activities.startTime)
            val date: Date = DateUtils().parseFormattedDate(dateString)
            groupedPair.value?.add(Pair(date, activities))
        }
    }

    private fun getQueriedActivitiesList(): ArrayList<ActivitiesModel> {
        val queriedActivitiesList: ArrayList<ActivitiesModel> = arrayListOf()
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