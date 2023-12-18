package com.kentreyhan.clocklify.activities.viewmodel

import android.icu.util.Calendar
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kentreyhan.clocklify.utils.DateUtils

class TimerViewModel : ViewModel() {
    val runningTimer: MutableLiveData<String?> = MutableLiveData<String?>()

    val startTimer: MutableLiveData<String?> = MutableLiveData<String?>()
    val startDate: MutableLiveData<String?> = MutableLiveData<String?>()

    val endTimer: MutableLiveData<String?> = MutableLiveData<String?>()
    val endDate: MutableLiveData<String?> = MutableLiveData<String?>()

    val coordinates: MutableLiveData<String?> = MutableLiveData<String?>()
    val activityDetail: MutableLiveData<String?> = MutableLiveData<String?>()

    private val interval: Int = 1000
    private var timerHandler: Handler? = null
    private var second: Long = 0L

    fun startTimer() {
        val time = Calendar.getInstance().time
        startTimer.value = DateUtils().getFormattedCurrentTime(time)
        startDate.value = DateUtils().getFormattedCurrentDate(time)
        timerHandler = Handler(Looper.getMainLooper())
        runTimer.run()
    }

    fun stopTimer() {
        timerHandler?.removeCallbacks(runTimer)
        val time = Calendar.getInstance().time
        endTimer.value = DateUtils().getFormattedCurrentTime(time)
        endDate.value = DateUtils().getFormattedCurrentDate(time)
    }

    fun resetTimer() {
        stopRunningTimer()
        resetStartDateTime()
        resetEndDateTime()
    }

    fun saveActivity() {
        //TODO: Save Activity in Local
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
                updateTimer(second)
            } finally {
                timerHandler!!.postDelayed(this, interval.toLong())
            }
        }
    }

    private fun updateTimer(time: Long) {
        val hours = time % 86400 / 3600
        val minutes = time % 86400 % 3600 / 60
        val seconds = time % 86400 % 3600 % 60

        runningTimer.value = String.format("%02d \t\t : \t\t %02d \t\t : \t\t %02d", hours, minutes, seconds)
    }

    private fun stopRunningTimer(){
        timerHandler?.removeCallbacks(runTimer)
        runningTimer.value = DEFAULT_TIMER
        second = 0L
    }

    private fun resetStartDateTime() {
        startTimer.value = "-"
        startDate.value = "-"
    }

    private fun resetEndDateTime() {
        endTimer.value = "-"
        endDate.value = "-"
    }

    companion object{
        private const val DEFAULT_TIMER: String = "00 \t\t : \t\t 00 \t\t : \t\t 00"
    }
}