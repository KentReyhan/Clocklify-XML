package com.kentreyhan.clocklify.activities.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.kentreyhan.clocklify.R
import com.kentreyhan.clocklify.activities.viewmodel.TimerViewModel
import com.kentreyhan.clocklify.databinding.FragmentTimerBinding
import com.kentreyhan.clocklify.utils.StringUtils

class TimerFragment : Fragment() {

    private lateinit var binding: FragmentTimerBinding

    private val vm: TimerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        initEvent()
        initObserver()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initEvent() {
        binding.startButton.setOnClickListener {
            vm.startTimer()
            setVisibleButton(goToRunning = true)
        }
        binding.stopButton.setOnClickListener {
            vm.stopTimer()
            setVisibleButton(goToEnd = true)
        }
        binding.resetButton.setOnClickListener {
            vm.resetTimer()
            setVisibleButton(goToStart = true)
        }
        binding.saveButton.setOnClickListener {
            vm.saveActivity()
            setVisibleButton(goToStart = true)
        }
        binding.deleteButton.setOnClickListener {
            vm.deleteActivity()
            setVisibleButton(goToStart = true)
        }

        binding.textBoxField.addTextChangedListener {
            vm.onActivitiesTextBoxChanged(binding.textBoxField.text.toString())
        }
    }

    private fun initObserver() {
        vm.runningTimer.observe(viewLifecycleOwner) { timer ->
            val str = StringUtils().checkIfStringValueExist(timer, resources.getString(R.string.timer_start))
            binding.timerText.text = str
        }

        vm.startTimer.observe(viewLifecycleOwner) { startTime ->
            val str = StringUtils().checkIfStringValueExist(startTime, "-")
            binding.startTimerText.text = str
        }

        vm.startDate.observe(viewLifecycleOwner) { startDate ->
            val str = StringUtils().checkIfStringValueExist(startDate, "-")
            binding.startDateText.text = str
        }

        vm.endTimer.observe(viewLifecycleOwner) { endTime ->
            val str = StringUtils().checkIfStringValueExist(endTime, "-")
            binding.endTimerText.text = str
        }

        vm.endDate.observe(viewLifecycleOwner) { endDate ->
            val str = StringUtils().checkIfStringValueExist(endDate, "-")
            binding.endDateText.text = str
        }

        vm.coordinates.observe(viewLifecycleOwner) { coordinate ->
            val str = StringUtils().checkIfStringValueExist(coordinate, "-")
            binding.coordinateCardTimer.text = str
        }

    }

    private fun setVisibleButton(goToStart: Boolean? = null, goToRunning: Boolean? = null, goToEnd: Boolean? = null) {
        with(binding) {
            when {
                goToStart == true -> {
                    startButton.visibility = View.VISIBLE
                    runningTimerButtonLayout.visibility = View.GONE
                    endTimerButtonLayout.visibility = View.GONE
                }

                goToRunning == true -> {
                    startButton.visibility = View.GONE
                    runningTimerButtonLayout.visibility = View.VISIBLE
                    endTimerButtonLayout.visibility = View.GONE
                }

                goToEnd == true -> {
                    startButton.visibility = View.GONE
                    runningTimerButtonLayout.visibility = View.GONE
                    endTimerButtonLayout.visibility = View.VISIBLE
                }
            }
        }
    }

}