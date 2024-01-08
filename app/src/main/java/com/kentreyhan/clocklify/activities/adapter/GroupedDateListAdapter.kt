package com.kentreyhan.clocklify.activities.adapter

import android.graphics.Canvas
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dao.database.ActivityDatabase
import com.example.dao.model.Activity
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.kentreyhan.clocklify.R
import com.kentreyhan.clocklify.activities.model.ActivitiesModel
import com.kentreyhan.clocklify.activities.model.GroupedActivitiesModel
import com.kentreyhan.clocklify.databinding.GroupedDateListBinding
import com.kentreyhan.commons.utils.DateUtils
import com.kentreyhan.commons.utils.dpToPixel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class GroupedDateListAdapter(
    private val groupedList: ArrayList<GroupedActivitiesModel>,
    var onItemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<GroupedDateListAdapter.ViewHolder>() {

    private lateinit var db: ActivityDatabase

    interface ItemListener {
        fun deleteItem()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GroupedDateListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group: GroupedActivitiesModel = groupedList[position]
        holder.bind(group, groupedList[position].id, this)

    }

    override fun getItemCount(): Int {
        return groupedList.size
    }

    inner class ViewHolder(private val binding: GroupedDateListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: GroupedActivitiesModel, id: Int, groupedDateListAdapter: GroupedDateListAdapter) {

            val decorator = MaterialDividerItemDecoration(binding.root.context, LinearLayoutManager.VERTICAL)
            decorator.apply {
                dividerThickness = binding.root.context.dpToPixel(1f).toInt()
                dividerColor = ContextCompat.getColor(binding.root.context, R.color.light_primary)
                dividerInsetStart = binding.root.context.dpToPixel(16f).toInt()
                dividerInsetEnd = binding.root.context.dpToPixel(16f).toInt()
                isLastItemDecorated = false
            }

            val rvAdapter = ActivitiesAdapter(group.activitiesList, onItemClickListener)
            binding.groupedDateText.text = DateUtils().getFormattedDate(group.date)
            binding.groupedDateRecyclerView.apply {
                layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.VERTICAL, false)
                adapter = rvAdapter

                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }

                    override fun onChildDraw(
                        canvas: Canvas,
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        dX: Float,
                        dY: Float,
                        actionState: Int,
                        isCurrentlyActive: Boolean
                    ) {

                        RecyclerViewSwipeDecorator.Builder(
                            canvas, recyclerView, viewHolder, dX, dY, actionState,
                            isCurrentlyActive
                        )
                            .addBackgroundColor(ContextCompat.getColor(context, R.color.light_red))
                            .addSwipeLeftLabel("DELETE")
                            .setSwipeLeftLabelColor(ContextCompat.getColor(context, R.color.white))
                            .setSwipeLeftLabelTypeface(
                                ResourcesCompat.getFont(
                                    context, com.kentreyhan.commons.R.font
                                        .roboto_regular
                                )
                            )
                            .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                            .create()
                            .decorate()


                        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                        val activityId = group.activitiesList[viewHolder.absoluteAdapterPosition].id
                        group.activitiesList.removeAt(viewHolder.absoluteAdapterPosition)


                        db = ActivityDatabase.getDatabase(context)
                        GlobalScope.launch {
                            db.activityDao().deleteById(activityId)
                        }

                        rvAdapter.notifyItemRemoved(viewHolder.absoluteAdapterPosition)

                        if (group.activitiesList.size == 0) {
                            val position = groupedDateListAdapter.groupedList.indexOfFirst { group -> id == group.id }
                            groupedDateListAdapter.groupedList.removeAt(position)
                            groupedDateListAdapter.notifyItemRemoved(position)
                        }
                    }
                }).attachToRecyclerView(binding.groupedDateRecyclerView)

            }.addItemDecoration(decorator)

        }
    }

}