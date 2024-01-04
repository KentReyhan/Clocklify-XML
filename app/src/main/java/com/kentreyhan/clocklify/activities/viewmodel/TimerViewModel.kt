package com.kentreyhan.clocklify.activities.viewmodel

import android.icu.util.Calendar
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kentreyhan.clocklify.activities.model.ActivitiesModel
import com.kentreyhan.clocklify.repository.ActivityRepository
import com.kentreyhan.clocklify.utils.DateUtils
import java.util.Date

class TimerViewModel : ViewModel() {

    val runningTimer: MutableLiveData<String?> = MutableLiveData<String?>()

    val startTimer: MutableLiveData<String?> = MutableLiveData<String?>()
    val startDate: MutableLiveData<String?> = MutableLiveData<String?>()

    val endTimer: MutableLiveData<String?> = MutableLiveData<String?>()
    val endDate: MutableLiveData<String?> = MutableLiveData<String?>()

    val coordinates: MutableLiveData<String?> = MutableLiveData<String?>()
    val activityDetail: MutableLiveData<String?> = MutableLiveData<String?>()

    var latitude: Double? = null
    var longitude: Double? = null

    var startDateTime: Date? = null
    var endDateTime: Date? = null

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

    fun saveActivity() {
        //TODO: Save Activity in Local

        formatTimer(second, true)

        ActivityRepository.addItem(
            ActivitiesModel(
                id = if (ActivityRepository.getSize() == 0) {
                    1
                } else {
                    ActivityRepository.getId() + 1
                },
                timer = runningTimer.value.toString(),
                activitiesDetail = activityDetail.value.toString(),
                startTime = startDateTime!!,
                endTime = endDateTime!!,
                latitude = latitude!!,
                longitude = longitude!!,
            )
        )

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
                formatTimer(second, false)
            } finally {
                timerHandler!!.postDelayed(this, interval.toLong())
            }
        }
    }

    private fun formatTimer(time: Long, isSaved: Boolean) {
        val hours = time % 86400 / 3600
        val minutes = time % 86400 % 3600 / 60
        val seconds = time % 86400 % 3600 % 60

        if (isSaved) {
            runningTimer.value = String.format("%02d : %02d : %02d", hours, minutes, seconds)
            return
        }

        runningTimer.value = String.format("%02d \t\t : \t\t %02d \t\t : \t\t %02d", hours, minutes, seconds)
    }

    private fun stopRunningTimer() {
        timerHandler?.removeCallbacks(runTimer)
        runningTimer.value = DEFAULT_TIMER
        second = 0L
    }

    private fun resetStartDateTime() {
        startTimer.value = "-"
        startDate.value = "-"
        startDateTime = null
    }

    private fun resetEndDateTime() {
        endTimer.value = "-"
        endDate.value = "-"
        endDateTime = null
    }

    companion object {
        private const val DEFAULT_TIMER: String = "00 \t\t : \t\t 00 \t\t : \t\t 00"
    }

}