package com.kentreyhan.clocklify.activities.fragment

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.kentreyhan.clocklify.R
import com.kentreyhan.clocklify.activities.viewmodel.TimerViewModel
import com.kentreyhan.clocklify.databinding.FragmentTimerBinding
import com.kentreyhan.commons.utils.LocationUtils
import com.kentreyhan.commons.utils.StringUtils
import com.kentreyhan.commons.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TimerFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: FragmentTimerBinding

    private val timerVM: TimerViewModel by viewModels()

    private val locationUtils: LocationUtils = LocationUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTimerBinding.inflate(inflater, container, false)
        initEvent()
        initObserver()
        getCoordinate()
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initEvent() {
        binding.startButton.setOnClickListener {
            timerVM.startTimer()
            setVisibleButton(goToRunning = true)

        }
        binding.stopButton.setOnClickListener {
            timerVM.stopTimer()
            setVisibleButton(goToEnd = true)
        }
        binding.resetButton.setOnClickListener {
            timerVM.resetTimer()
            setVisibleButton(goToStart = true)
        }
        binding.saveButton.setOnClickListener {
            if (timerVM.activityDetail.value.isNullOrBlank()) {
                ToastUtils().showToast(requireContext(),"Text box must be filled to be able to save activity")
                return@setOnClickListener
            }

            timerVM.saveActivity(requireContext())
            setVisibleButton(goToStart = true)
        }
        binding.deleteButton.setOnClickListener {
            timerVM.deleteActivity()
            setVisibleButton(goToStart = true)
        }

        binding.textBoxField.addTextChangedListener {
            timerVM.onActivitiesTextBoxChanged(binding.textBoxField.text.toString())
        }
    }

    private fun initObserver() {
        timerVM.runningTimer.observe(viewLifecycleOwner) { timer ->
            val str = StringUtils().checkIfStringValueExist(timer, resources.getString(R.string.timer_start))
            binding.timerText.text = str
        }

        timerVM.startTimer.observe(viewLifecycleOwner) { startTime ->
            val str = StringUtils().checkIfStringValueExist(startTime, "-")
            binding.startTimerText.text = str
        }

        timerVM.startDate.observe(viewLifecycleOwner) { startDate ->
            val str = StringUtils().checkIfStringValueExist(startDate, "-")
            binding.startDateText.text = str
        }

        timerVM.endTimer.observe(viewLifecycleOwner) { endTime ->
            val str = StringUtils().checkIfStringValueExist(endTime, "-")
            binding.endTimerText.text = str
        }

        timerVM.endDate.observe(viewLifecycleOwner) { endDate ->
            val str = StringUtils().checkIfStringValueExist(endDate, "-")
            binding.endDateText.text = str
        }

        timerVM.coordinates.observe(viewLifecycleOwner) { coordinate ->
            val str = StringUtils().checkIfStringValueExist(coordinate, "-")
            binding.coordinateCardTimer.text = str
        }

        timerVM.isLoading.observe(viewLifecycleOwner) { loading ->
                if (loading == true) {
                    binding.loadingIndicator.visibility = View.VISIBLE
                } else {
                    binding.loadingIndicator.visibility = View.INVISIBLE
                }
            }



    }

    private fun setVisibleButton(goToStart: Boolean? = null, goToRunning: Boolean? = null, goToEnd: Boolean? =
    null) {
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

    private fun getCoordinate() {
        if (!locationUtils.checkForFineLocationPermission(requireContext())) {
            Toast.makeText(
                requireContext(),
                "Coordinate cannot be acquired, please enable access for GPS in setting",
                Toast.LENGTH_SHORT
            ).show()
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
            override fun isCancellationRequested() = false
        }).addOnSuccessListener { location: Location? ->
            if (location == null)
                Toast.makeText(
                    requireContext(),
                    "Coordinate cannot be acquired, please check if location are enabled",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                timerVM.latitude = location.latitude
                timerVM.longitude = location.longitude
                timerVM.coordinates.value = String.format("%f.%f", timerVM.latitude, timerVM.longitude)
            }

        }
    }

}