package com.kentreyhan.clocklify.activitiesDetail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dao.database.ActivityDatabase
import com.example.dao.model.Activity
import com.kentreyhan.clocklify.activities.model.ActivitiesModel
import com.kentreyhan.clocklify.activitiesDetail.viewmodel.ActivitiesDetailViewModel
import com.kentreyhan.clocklify.databinding.FragmentTimerBinding
import com.kentreyhan.clocklify.repository.ActivityRepository
import com.kentreyhan.commons.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ActivitiesDetailFragment(var activity: Activity) : Fragment() {
    private lateinit var binding: FragmentTimerBinding

    private lateinit var db: ActivityDatabase

    private val activitiesDetailVM: ActivitiesDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitiesDetailVM.activity.value = activity
        //db = ActivityDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTimerBinding.inflate(inflater, container, false)
        initValue()
        initEvent()
        initObserver()
        setVisibleButton()
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initValue() {
        binding.timerText.text = activity.timer
        binding.startTimerText.text = activity.startTime?.let { DateUtils().getFormattedTime(it) }
        binding.startDateText.text = activity.startTime?.let { DateUtils().getFormattedDate(it) }
        binding.endTimerText.text = activity.endTime?.let { DateUtils().getFormattedTime(it) }
        binding.endDateText.text = activity.endTime?.let { DateUtils().getFormattedDate(it) }
        binding.coordinateCardTimer.text = String.format("%f.%f", activity.latitude, activity.longitude)
        binding.textBoxField.setText(activity.activitiesDetail)
        activitiesDetailVM.activityDetail.value = activity.activitiesDetail
    }

    private fun initEvent() {
        binding.textBoxField.addTextChangedListener {
            activitiesDetailVM.onActivitiesTextBoxChanged(binding.textBoxField.text.toString())
        }

        binding.saveButton.setOnClickListener {
            activitiesDetailVM.updateActivity(requireContext())
        }

        binding.deleteButton.setOnClickListener {
            activitiesDetailVM.deleteActivity(requireContext())
        }
    }

    private fun initObserver(){
        activitiesDetailVM.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading == true) {
                binding.loadingIndicator.visibility = View.VISIBLE
            } else {
                binding.loadingIndicator.visibility = View.INVISIBLE
            }
        }

        activitiesDetailVM.isFinished.observe(viewLifecycleOwner) { finish ->
            if (finish == true) {
                requireActivity().finish()
            }
        }
    }

    private fun setVisibleButton() {
        with(binding) {
            startButton.visibility = View.GONE
            runningTimerButtonLayout.visibility = View.GONE
            endTimerButtonLayout.visibility = View.VISIBLE
        }
    }

}