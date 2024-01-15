package com.example.network.api

import android.content.Context
import com.example.dao.token.TokenUtils
import okhttp3.Interceptor
import okhttp3.Response

class InterceptorBuilder(context: Context) : Interceptor {
    private val appContext = context
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenUtils(appContext).getToken().toString()
        return if (token.isNotEmpty()) {
            val request = chain
                .request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            chain.proceed(request)
        } else {
            chain.proceed(chain.request())
        }
    }

}