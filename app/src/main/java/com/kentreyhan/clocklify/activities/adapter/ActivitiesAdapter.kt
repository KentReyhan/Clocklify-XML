package com.kentreyhan.clocklify.activities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kentreyhan.clocklify.activities.model.ActivitiesModel
import com.kentreyhan.clocklify.databinding.ActivitiesListViewBinding
import com.kentreyhan.clocklify.utils.DateUtils

class ActivitiesAdapter(private val activitiesList: List<ActivitiesModel>, var onItemClickListener: (ActivitiesModel) ->
Unit) :
    RecyclerView.Adapter<ActivitiesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActivitiesListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group: ActivitiesModel = activitiesList[position]
        holder.bind(group)
        holder.itemView.setOnClickListener {
            onItemClickListener(activitiesList[position])
        }
    }

    override fun getItemCount(): Int {
        return activitiesList.size
    }

    inner class ViewHolder(val binding: ActivitiesListViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(activities: ActivitiesModel) {
            binding.activitiesTimerText.text = activities.timer
            binding.activitiesDetailText.text = activities.activitiesDetail
            binding.activitiesStartEndTimeText.text = String.format(
                " %s - %s", DateUtils().getFormattedTime
                    (activities.startTime), DateUtils().getFormattedTime(activities.endTime)
            )
            binding.activitiesCoordinatesText.text = String.format("%s.%s", activities.latitude, activities.longitude)

        }
    }

}