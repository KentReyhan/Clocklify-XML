package com.kentreyhan.clocklify.activitiesDetail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kentreyhan.clocklify.activities.model.ActivitiesModel
import com.kentreyhan.clocklify.activitiesDetail.viewmodel.ActivitiesDetailViewModel
import com.kentreyhan.clocklify.databinding.FragmentTimerBinding
import com.kentreyhan.clocklify.repository.ActivityRepository
import com.kentreyhan.clocklify.utils.DateUtils

class ActivitiesDetailFragment(var activity: ActivitiesModel) : Fragment() {
    private lateinit var binding: FragmentTimerBinding

    private val activitiesDetailVM: ActivitiesDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTimerBinding.inflate(inflater, container, false)
        initValue()
        initEvent()
        setVisibleButton()
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initValue() {
        binding.timerText.text = activity.timer
        binding.startTimerText.text = DateUtils().getFormattedTime(activity.startTime)
        binding.startDateText.text = DateUtils().getFormattedDate(activity.startTime)
        binding.endTimerText.text = DateUtils().getFormattedTime(activity.endTime)
        binding.endDateText.text = DateUtils().getFormattedDate(activity.endTime)
        binding.coordinateCardTimer.text = String.format("%f.%f", activity.latitude, activity.longitude)
        binding.textBoxField.setText(activity.activitiesDetail)
        activitiesDetailVM.activityDetail.value = activity.activitiesDetail
    }

    private fun initEvent() {
        binding.textBoxField.addTextChangedListener {
            activitiesDetailVM.onActivitiesTextBoxChanged(binding.textBoxField.text.toString())
        }

        binding.saveButton.setOnClickListener {
            saveActivitiesChanges()
        }

        binding.deleteButton.setOnClickListener {
            deleteActivities()
        }
    }

    private fun setVisibleButton(goToStart: Boolean? = null, goToRunning: Boolean? = null, goToEnd: Boolean? = null) {
        with(binding) {
            startButton.visibility = View.GONE
            runningTimerButtonLayout.visibility = View.GONE
            endTimerButtonLayout.visibility = View.VISIBLE
        }
    }

    private fun saveActivitiesChanges(){
        ActivityRepository.updateDetailItem(activity.id,activitiesDetailVM.activityDetail.value.toString())
        requireActivity().finish()
    }

    private fun deleteActivities(){
        ActivityRepository.removeItemById(activity.id)
        requireActivity().finish()
    }

}