package com.kentreyhan.clocklify.activitiesDetail.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dao.model.Activity
import com.kentreyhan.clocklify.activitiesDetail.fragment.ActivitiesDetailFragment
import com.kentreyhan.clocklify.activitiesDetail.viewmodel.ActivitiesDetailViewModel
import com.kentreyhan.clocklify.databinding.ActivityActivitiesDetailBinding
import com.kentreyhan.commons.utils.DateUtils


class ActivitiesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActivitiesDetailBinding

    //private val db: ActivityDatabase = ActivityDatabase.getDatabase(this@ActivitiesDetailActivity)

    private val activitiesDetailVM: ActivitiesDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivitiesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("Selected Activities", -1)

        if (id == -1) {
            Toast.makeText(
                this@ActivitiesDetailActivity, "Error, cannot fetch selected activities", Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        activitiesDetailVM.getSelectedActivity(id,this)

        /*runBlocking {
            CoroutineScope(Dispatchers.IO).async {
                activity = db.activityDao().getActivityById(id)
            }.await()
        }*/
        initObserve()
    }

    private fun initObserve() {
        activitiesDetailVM.isLoading.observe(this) { loading ->
            if (loading == true) {
                binding.loadingIndicator.visibility = View.VISIBLE
            } else {
                binding.loadingIndicator.visibility = View.INVISIBLE
            }
        }

        activitiesDetailVM.activity.observe(this) { selectedActivity ->
            if (selectedActivity != null) {
                binding.detailsDateText.text = selectedActivity.startTime?.let { DateUtils()
                    .getFormattedDate(it) }

                supportFragmentManager.beginTransaction().apply {
                    replace(binding.activitiesDetailFrameLayout.id, ActivitiesDetailFragment(selectedActivity))
                    addToBackStack(null)
                }.commit()
            }
        }
    }

}