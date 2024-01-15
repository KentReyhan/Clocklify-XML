package com.kentreyhan.clocklify.activitiesDetail.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dao.model.Activity
import com.example.network.api.ApiServiceBuilder
import com.example.network.dto.activity.request.ActivityRequest
import com.example.network.dto.activity.response.ActivityResponse
import com.example.network.dto.activity.response.ActivitySingleResponse
import com.example.network.dto.activity.response.MessageResponse
import com.example.network.dto.activity.response.toModel
import com.example.network.service.ActivityService
import com.kentreyhan.commons.utils.DateUtils
import com.kentreyhan.commons.utils.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivitiesDetailViewModel : ViewModel() {

    val activityDetail: MutableLiveData<String> = MutableLiveData<String>()

    val activity: MutableLiveData<Activity> = MutableLiveData<Activity>()

    private lateinit var activityService: ActivityService

    val isLoading: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()
    val isFinished: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()

    fun onIsLoadingChanged(value: Boolean) {
        isLoading.postValue(value)
    }

    fun onIsFinishedChanged(value: Boolean) {
        isFinished.postValue(value)
    }

    fun onActivitiesTextBoxChanged(details: String) {
        activityDetail.value = details
    }

    fun getSelectedActivity(id: Int, context: Context) {
        activityService = ApiServiceBuilder.getActivityInstance(context)

        onIsLoadingChanged(true)
        activityService.getActivityById(id = id.toString())
            .enqueue(object : Callback<ActivitySingleResponse> {
                override fun onFailure(call: Call<ActivitySingleResponse>, t: Throwable) {
                    ToastUtils().showToast(context, "Get Selected Activity Failed 1")
                    onIsLoadingChanged(false)
                    return
                }

                override fun onResponse(call: Call<ActivitySingleResponse>, response: Response<ActivitySingleResponse>) {
                    val activityResponse = response.body()
                    if (activityResponse == null) {
                        ToastUtils().showToast(context, "Get Selected Activity Failed 2")
                        onIsLoadingChanged(false)
                        return
                    }
                    activity.postValue(activityResponse.toModel())
                    onIsLoadingChanged(false)
                }
            })
    }

    fun deleteActivity(context: Context) {
        activityService = ApiServiceBuilder.getActivityInstance(context)

        onIsLoadingChanged(true)
        if(activity.value==null){
            ToastUtils().showToast(context, "Update Activity Failed, Data Does Not Exist")
            onIsLoadingChanged(false)
            return
        }
        activityService.deleteActivity(id = activity.value?.id.toString())
            .enqueue(object : Callback<MessageResponse> {
                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    ToastUtils().showToast(context, "Delete Activity Failed")
                    onIsLoadingChanged(false)
                    return
                }

                override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                    val activityResponse = response.body()
                    if (activityResponse == null) {
                        ToastUtils().showToast(context, "Delete Activity Failed")
                        onIsLoadingChanged(false)
                        return
                    }
                    onIsLoadingChanged(false)
                    onIsFinishedChanged(true)
                }
            })
    }

    fun updateActivity(context: Context) {
        activityService = ApiServiceBuilder.getActivityInstance(context)

        onIsLoadingChanged(true)
        if(activity.value==null || activity.value?.startTime==null||activity.value?.endTime==null){
            ToastUtils().showToast(context, "Update Activity Failed, Data Does Not Exist or Date and Time is Invalid")
            onIsLoadingChanged(false)
            return
        }
        activityService.editActivity(
            id = activity.value?.id.toString(),
            request = ActivityRequest(
                datestart = DateUtils().getFormattedRequestedDate(activity.value?.startTime!!),
                dateend = DateUtils().getFormattedRequestedDate(activity.value?.endTime!!),
                activity = activityDetail.value,
                duration = activity.value?.timer,
                timestart = DateUtils().getFormattedRequestedTime(activity.value?.startTime!!),
                timeend = DateUtils().getFormattedRequestedTime(activity.value?.endTime!!),
                locationlong = activity.value?.longitude,
                locationlat = activity.value?.latitude
            )
        )
            .enqueue(object : Callback<MessageResponse> {
                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    ToastUtils().showToast(context, "Update Activity Failed")
                    onIsLoadingChanged(false)
                    return
                }

                override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                    val activityResponse = response.body()
                    if (activityResponse == null) {
                        ToastUtils().showToast(context, "Update Activity Failed")
                        onIsLoadingChanged(false)
                        return
                    }
                    onIsLoadingChanged(false)
                    onIsFinishedChanged(true)
                }
            })
    }
}