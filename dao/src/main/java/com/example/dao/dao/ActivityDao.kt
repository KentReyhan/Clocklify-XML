package com.example.dao.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dao.model.Activity

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity_table")
    fun getAllActivity(): List<Activity>

    @Query("SELECT * FROM activity_table WHERE id = :id LIMIT 1")
    suspend fun getActivityById(id: Int) : Activity

    @Query("SELECT * FROM activity_table WHERE activities_detail LIKE '%' || :query || '%' ")
    suspend fun findByQuery(query:String): List<Activity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addActivity(activity: Activity)

    @Delete
    suspend fun deleteByActivity(activity: Activity)

    @Query("DELETE FROM activity_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM activity_table")
    suspend fun deleteAllActivity()

    @Query("UPDATE activity_table SET activities_detail = :activitiesDetail WHERE id LIKE " +
            ":id")
    suspend fun updateActivityDetail(activitiesDetail: String, id: Int)
}