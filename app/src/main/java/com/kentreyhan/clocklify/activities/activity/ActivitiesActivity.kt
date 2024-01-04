package com.kentreyhan.clocklify.activities.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.kentreyhan.clocklify.activities.fragment.ActivitiesFramePageAdapter
import com.kentreyhan.clocklify.databinding.ActivityActivitiesBinding
import com.kentreyhan.clocklify.utils.LocationUtils

class ActivitiesActivity : AppCompatActivity() {
    private lateinit var fragmentPageAdapter: ActivitiesFramePageAdapter

    private lateinit var binding: ActivityActivitiesBinding

    private val locationUtils: LocationUtils = LocationUtils()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!locationUtils.checkForFineLocationPermission(this)){
            locationUtils.askForFineLocationPermission(this)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })

        fragmentPageAdapter = ActivitiesFramePageAdapter(supportFragmentManager, lifecycle)
        initTab()
    }

    private fun initTab() {
        with(binding) {
            activitiesViewPager.adapter = fragmentPageAdapter

            TabLayoutMediator(activitiesTabLayout, activitiesViewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "TIMER"
                    1 -> tab.text = "ACTIVITY"
                }
            }.attach()

            activitiesViewPager.isUserInputEnabled = false
        }

    }

}