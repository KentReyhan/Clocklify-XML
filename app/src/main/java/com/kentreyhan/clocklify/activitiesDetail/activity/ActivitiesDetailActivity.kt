package com.kentreyhan.clocklify.activitiesDetail.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dao.database.ActivityDatabase
import com.example.dao.model.Activity
import com.kentreyhan.clocklify.activitiesDetail.fragment.ActivitiesDetailFragment
import com.kentreyhan.clocklify.databinding.ActivityActivitiesDetailBinding
import com.kentreyhan.commons.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class ActivitiesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActivitiesDetailBinding

    private val db: ActivityDatabase = ActivityDatabase.getDatabase(this@ActivitiesDetailActivity)

    private lateinit var activity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivitiesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("Selected Activities",-1)

        if(id==-1){
            Toast.makeText(
                this@ActivitiesDetailActivity,
                "Error, cannot fetch selected activities",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        runBlocking {
            CoroutineScope(Dispatchers.IO).async {
                activity = db.activityDao().getActivityById(id)
            }.await()
        }

        binding.detailsDateText.text = DateUtils().getFormattedDate(activity.startTime)

        supportFragmentManager.beginTransaction().apply {
            replace(binding.activitiesDetailFrameLayout.id, ActivitiesDetailFragment(activity))
            addToBackStack(null)
        }.commit()
    }

}