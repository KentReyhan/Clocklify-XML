package com.example.network.api

import android.content.Context
import com.example.network.service.ActivityService
import com.example.network.service.AuthService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiServiceBuilder {
    private val baseUrl: String = "https://8cfe-36-69-90-62.ngrok-free.app"
    private fun getRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(
                InterceptorBuilder(context)
            ).addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
    }

    companion object {
        @Volatile
        private var authInstance: AuthService? = null
        private var activityInstance: ActivityService? = null

        fun getAuthInstance(context: Context): AuthService {
            val tempInstance = authInstance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = ApiServiceBuilder().getRetrofit(context).create(AuthService::class.java)
                authInstance = instance
                return instance
            }
        }

        fun getActivityInstance(context: Context): ActivityService {
            val tempInstance = activityInstance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = ApiServiceBuilder().getRetrofit(context).create(ActivityService::class.java)
                activityInstance = instance
                return instance
            }
        }
    }
}