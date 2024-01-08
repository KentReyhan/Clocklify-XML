package com.kentreyhan.clocklify.activities.viewmodel

import android.content.Context
import android.icu.util.Calendar
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dao.database.ActivityDatabase
import com.example.dao.model.Activity
import com.kentreyhan.clocklify.activities.model.ActivitiesModel
import com.kentreyhan.clocklify.repository.ActivityRepository
import com.kentreyhan.commons.utils.DateUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    var startDateTime: Date = Date()
    var endDateTime: Date = Date()

    private val interval: Int = 1000
    private var timerHandler: Handler? = null
    private var second: Long = 0L

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
        db = ActivityDatabase.getDatabase(context)

        Log.d("timer", runningTimer.value.toString())
        GlobalScope.launch {
            val formattedTimer = formatTimer(second, true)

            db.activityDao().addActivity(
                Activity(
                    timer = formattedTimer,
                    activitiesDetail = activityDetail.value.toString(),
                    startTime = startDateTime,
                    endTime = endDateTime,
                    latitude = latitude,
                    longitude = longitude,
                )
            )
        }

        stopRunningTimer()
        resetStartDateTime()
        resetEndDateTime()
    }

    fun deleteActivity() {
        stopRunningTimer()
        resetStartDateTime()
        resetEndDateTime()
    }

    fun onActivitiesTextBoxChanged(details: String) {
        activityDetail.value = details
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
            return String.format("%02d : %02d : %02d", hours, minutes, seconds)
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