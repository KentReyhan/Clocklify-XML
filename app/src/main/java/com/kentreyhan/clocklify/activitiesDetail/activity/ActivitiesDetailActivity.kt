package com.kentreyhan.clocklify.activitiesDetail.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.kentreyhan.clocklify.activities.fragment.ActivitiesFramePageAdapter
import com.kentreyhan.clocklify.activities.model.ActivitiesModel
import com.kentreyhan.clocklify.activitiesDetail.fragment.ActivitiesDetailFragment
import com.kentreyhan.clocklify.databinding.ActivityActivitiesDetailBinding
import com.kentreyhan.clocklify.utils.DateUtils


class ActivitiesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActivitiesDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivitiesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activities = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra("Selected Activities", ActivitiesModel::class.java)!!
        else
            intent.getSerializableExtra("Selected Activities") as ActivitiesModel

        binding.detailsDateText.text = DateUtils().getFormattedDate(activities.startTime)

        supportFragmentManager.beginTransaction().apply {
            replace(binding.activitiesDetailFrameLayout.id, ActivitiesDetailFragment(activities))
            addToBackStack(null)
        }.commit()
    }

}