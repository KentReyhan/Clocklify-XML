package com.example.network.service

import com.example.network.dto.auth.request.LoginRequest
import com.example.network.dto.auth.request.RegisterRequest
import com.example.network.dto.auth.response.LoginResponse
import com.example.network.dto.auth.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface AuthService {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

}