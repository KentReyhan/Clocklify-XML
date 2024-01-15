package com.kentreyhan.clocklify.activities.viewmodel

import android.content.Context
import android.icu.util.Calendar
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dao.database.ActivityDatabase
import com.example.dao.model.Activity
import com.example.network.api.ApiServiceBuilder
import com.example.network.dto.activity.request.ActivityRequest
import com.example.network.dto.activity.response.ActivityListResponse
import com.example.network.dto.activity.response.MessageResponse
import com.example.network.dto.activity.response.toModel
import com.example.network.service.ActivityService
import com.kentreyhan.commons.utils.DateUtils
import com.kentreyhan.commons.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class TimerViewModel : ViewModel() {

    private lateinit var db: ActivityDatabase

    val runningTimer: MutableLiveData<String?> = MutableLiveData<String?>()

    val startTimer: MutableLiveData<String?> = MutableLiveData<String?>()
    val startDate: MutableLiveData<String?> = MutableLiveData<String?>()

    val endTimer: MutableLiveData<String?> = MutableLiveData<String?>()
    val endDate: MutableLiveData<String?> = MutableLiveData<String?>()

    val coordinates: MutableLiveData<String?> = MutableLiveData<String?>()
    val activityDetail: MutableLiveData<String?> = MutableLiveData<String?>()

    val isLoading: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()

    private lateinit var activityService: ActivityService

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    var startDateTime: Date = Date()
    var endDateTime: Date = Date()

    private val interval: Int = 1000
    private var timerHandler: Handler? = null
    private var second: Long = 0L



    fun onActivitiesTextBoxChanged(details: String) {
        activityDetail.value = details
    }

    fun onIsLoadingChanged(value: Boolean) {
        isLoading.postValue(value)
    }

    fun startTimer() {
        val time = Calendar.getInstance().time
        startDateTime = time
        startTimer.value = DateUtils().getFormattedTime(time)
        startDate.value = DateUtils().getFormattedDate(time)
        timerHandler = Handler(Looper.getMainLooper())
        runTimer.run()
    }

    fun stopTimer() {
        timerHandler?.removeCallbacks(runTimer)
        val time = Calendar.getInstance().time
        endDateTime = time
        endTimer.value = DateUtils().getFormattedTime(time)
        endDate.value = DateUtils().getFormattedDate(time)
    }

    fun resetTimer() {
        stopRunningTimer()
        resetStartDateTime()
        resetEndDateTime()
    }

    fun saveActivity(context: Context) {
        val formattedTimer = formatTimer(second, true)
        activityService = ApiServiceBuilder.getActivityInstance(context)
        onIsLoadingChanged(true)
        activityService.createActivity(ActivityRequest(
            datestart = DateUtils().getFormattedRequestedDate(startDateTime),
            dateend = DateUtils().getFormattedRequestedDate(endDateTime),
            activity = activityDetail.value,
            duration = formattedTimer,
            timestart = DateUtils().getFormattedRequestedTime(startDateTime),
            timeend = DateUtils().getFormattedRequestedTime(endDateTime),
            locationlong = longitude,
            locationlat = latitude
            ))
            .enqueue(object : Callback<MessageResponse> {
            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                ToastUtils().showToast(context, "Fetching Activity List Failed")
                onIsLoadingChanged(false)
                return
            }

            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                val activityResponse = response.body()
                if (activityResponse == null) {
                    ToastUtils().showToast(context, "Fetching Activity List Failed")
                    onIsLoadingChanged(false)
                    return
                }
                onIsLoadingChanged(false)
            }
        })
        /*db = ActivityDatabase.getDatabase(context)
        Log.d("timer", runningTimer.value.toString())
        CoroutineScope(Dispatchers.IO).launch {
            db.activityDao().addActivity(
                Activity(
                    id = null,
                    timer = formattedTimer,
                    startTime = startDateTime,
                    endTime = endDateTime,
                    longitude = longitude,
                    latitude = latitude,
                    activitiesDetail = activityDetail.value
                )
            )
            Log.d("length", db.activityDao().getTableCount().toString())
        }*/

        stopRunningTimer()
        resetStartDateTime()
        resetEndDateTime()
    }

    fun deleteActivity() {
        stopRunningTimer()
        resetStartDateTime()
        resetEndDateTime()
    }

    private var runTimer: Runnable = object : Runnable {
        override fun run() {
            try {
                second += 1
                runningTimer.value = formatTimer(second, false)
            } finally {
                timerHandler!!.postDelayed(this, interval.toLong())
            }
        }
    }

    private fun formatTimer(time: Long, isSaved: Boolean): String {
        val hours = time % 86400 / 3600
        val minutes = time % 86400 % 3600 / 60
        val seconds = time % 86400 % 3600 % 60

        if (isSaved) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        return String.format("%02d \t\t : \t\t %02d \t\t : \t\t %02d", hours, minutes, seconds)
    }

    private fun stopRunningTimer() {
        timerHandler?.removeCallbacks(runTimer)
        runningTimer.value = DEFAULT_TIMER
        second = 0L
    }

    private fun resetStartDateTime() {
        startTimer.value = "-"
        startDate.value = "-"
        startDateTime = Date()
    }

    private fun resetEndDateTime() {
        endTimer.value = "-"
        endDate.value = "-"
        endDateTime = Date()
    }

    companion object {
        private const val DEFAULT_TIMER: String = "00 \t\t : \t\t 00 \t\t : \t\t 00"
    }

}