package com.kentreyhan.clocklify.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.model.Activity
import com.kentreyhan.clocklify.activities.model.ActivitiesModel
import com.kentreyhan.clocklify.databinding.ActivitiesListViewBinding
import com.kentreyhan.commons.utils.DateUtils

class ActivitiesAdapter(private val activitiesList: List<Activity>, var onItemClickListener: (Int) ->
Unit) :
    RecyclerView.Adapter<ActivitiesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActivitiesListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group: Activity = activitiesList[position]
        holder.bind(group)
        holder.itemView.setOnClickListener {
            activitiesList[position].id?.let { it1 -> onItemClickListener(it1) }
        }
    }

    override fun getItemCount(): Int {
        return activitiesList.size
    }

    inner class ViewHolder(val binding: ActivitiesListViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activities: Activity) {
            binding.activitiesTimerText.text = activities.timer
            binding.activitiesDetailText.text = activities.activitiesDetail
            binding.activitiesStartEndTimeText.text = String.format(
                " %s - %s", activities.startTime?.let { DateUtils().getFormattedTime(it) },
                activities.endTime?.let { DateUtils().getFormattedTime(it) }
            )
            binding.activitiesCoordinatesText.text = String.format("%s.%s", activities.latitude, activities.longitude)

        }
    }

}