package com.kentreyhan.clocklify.activitiesDetail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivitiesDetailViewModel : ViewModel() {

    val activityDetail: MutableLiveData<String> = MutableLiveData<String>()


    fun onActivitiesTextBoxChanged(details: String) {
        activityDetail.value = details
    }
}