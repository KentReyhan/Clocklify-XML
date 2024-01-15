package com.example.network.service

import com.example.network.dto.activity.request.ActivityRequest
import com.example.network.dto.activity.response.ActivityListResponse
import com.example.network.dto.activity.response.ActivityResponse
import com.example.network.dto.activity.response.ActivitySingleResponse
import com.example.network.dto.activity.response.MessageResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ActivityService {
    @GET("viewactivity/{sort}")
   fun getAllActivity(@Path("sort") sort: String?): Call<ActivityListResponse>

    @GET("searchactivity/{order}/{search}")
    fun getActivityByQuery(@Path("order") order: String?, @Path("search") search: String?):
            Call<ActivityListResponse>

    @GET("viewactivitybyid/{id}")
    fun getActivityById(@Path("id") id: String?): Call<ActivitySingleResponse>

    @POST("saveactivity")
    fun createActivity(@Body request: ActivityRequest): Call<MessageResponse>

    @PUT("editactivity/{id}")
    fun editActivity(@Body request: ActivityRequest, @Path("id") id: String?): Call<MessageResponse>

    @DELETE("deleteactivity/{id}")
    fun deleteActivity(@Path("id") id: String?): Call<MessageResponse>
}