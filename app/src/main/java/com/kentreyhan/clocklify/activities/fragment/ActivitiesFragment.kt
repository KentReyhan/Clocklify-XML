package com.kentreyhan.clocklify.activities.fragment

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.model.Activity
import com.example.network.api.ApiServiceBuilder
import com.example.network.dto.activity.response.ActivityListResponse
import com.example.network.dto.activity.response.toModel
import com.example.network.service.ActivityService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.kentreyhan.clocklify.R
import com.kentreyhan.clocklify.activities.adapter.GroupedDateListAdapter
import com.kentreyhan.clocklify.activities.model.ActivitiesModel
import com.kentreyhan.clocklify.activities.viewmodel.ActivitiesViewModel
import com.kentreyhan.clocklify.activitiesDetail.activity.ActivitiesDetailActivity
import com.kentreyhan.clocklify.databinding.FragmentActivitiesBinding
import com.kentreyhan.commons.utils.LocationUtils
import com.kentreyhan.commons.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class ActivitiesFragment : Fragment(), GroupedDateListAdapter.ItemListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: FragmentActivitiesBinding

    private val activitiesVM: ActivitiesViewModel by viewModels()

    private val locationUtils: LocationUtils = LocationUtils()

    private val onItemClickListener: (Int) -> Unit = { activityId ->
        requireContext().startActivity(
            Intent(context, ActivitiesDetailActivity::class.java)
                .putExtra("Selected Activities", activityId)
        )
    }
    private lateinit var activitiesLayoutManager: LinearLayoutManager
    private lateinit var activitiesAdapter: GroupedDateListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitiesVM.fetchActivityList(requireContext())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivitiesBinding.inflate(inflater, container, false)

        val sortBy = resources.getStringArray(R.array.sort_by)
        val arrayAdapter = ArrayAdapter(binding.root.context, R.layout.dropdown_item, sortBy)
        binding.sortByDropdownList.setAdapter(arrayAdapter)
        binding.sortByDropdownList.setDropDownBackgroundDrawable(
            ContextCompat.getDrawable(
                binding.root.context, com.kentreyhan.commons.R.drawable.background_light_blue_rounded
            )
        )
        binding.sortByDropdownList.setText(sortBy[0], false)

        initEvent()
        initObserver()
        initRecyclerView()
        return binding.root
    }

    override fun onStart() {
        CoroutineScope(Dispatchers.IO).launch {
            activitiesVM.fetchActivityList(requireContext())
        }
        initRecyclerView()
        super.onStart()
    }

    override fun onResume() {
        CoroutineScope(Dispatchers.IO).launch {
            activitiesVM.fetchActivityList(requireContext())
        }
        initRecyclerView()
        super.onResume()
    }

    private fun initEvent() {
        getCoordinate()
        activitiesVM.sortByValue.observe(viewLifecycleOwner) {
            when (activitiesVM.sortByValue.value) {
                "Latest Date" -> {
                    activitiesVM.sortByDate()
                    initRecyclerView()
                }

                "Nearby" -> {
                    activitiesVM.sortByCoordinates()
                    initRecyclerView()
                }

                else -> {
                    return@observe
                }
            }
        }

        activitiesVM.searchKeyword.observe(viewLifecycleOwner) {
            when (activitiesVM.sortByValue.value) {
                "Latest Date" -> {
                    activitiesVM.sortByDate()
                    initRecyclerView()
                }

                "Nearby" -> {
                    getCoordinate()
                    activitiesVM.sortByCoordinates()
                    initRecyclerView()
                }

                else -> {
                    return@observe
                }
            }
        }

    }

    private fun initObserver() {
        activitiesVM.activitiesList.observe(viewLifecycleOwner){
            activitiesVM.sortByDate()
            activitiesVM.sortByCoordinates()
            initRecyclerView()
        }

        binding.searchActivityTextField.doAfterTextChanged {
            activitiesVM.onActivitiesSearchChanged(binding.searchActivityTextField.text.toString())
        }

        binding.sortByDropdownList.addTextChangedListener {
            activitiesVM.onDropdownValueChanged(binding.sortByDropdownList.text.toString())
        }

        activitiesVM.isLoading.observe(viewLifecycleOwner) { loading ->
            if (loading == true) {
                binding.loadingIndicator.visibility = View.VISIBLE
            } else {
                binding.loadingIndicator.visibility = View.INVISIBLE
            }
        }
    }


    private fun initRecyclerView() {
        activitiesLayoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
        activitiesAdapter = GroupedDateListAdapter(activitiesVM.getGroupedList(), onItemClickListener)

        binding.activitiesRecyclerView.apply {
            layoutManager = activitiesLayoutManager
            adapter = activitiesAdapter
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
            if (location == null) Toast.makeText(
                requireContext(),
                "Coordinate cannot be acquired, please check if location are enabled",
                Toast.LENGTH_SHORT
            ).show()
            else {
                activitiesVM.coordinate.value = arrayListOf(location.latitude, location.longitude)
            }
        }
    }

    override fun deleteItem() {
        activitiesAdapter.notifyItemRemoved(1)
    }

}