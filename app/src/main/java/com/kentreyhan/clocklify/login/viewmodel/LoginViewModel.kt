package com.kentreyhan.clocklify.login.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dao.token.TokenUtils
import com.example.network.api.ApiServiceBuilder
import com.kentreyhan.clocklify.activities.activity.ActivitiesActivity
import com.kentreyhan.clocklify.register.activity.RegisterActivity
import com.kentreyhan.clocklify.login.activity.LoginPasswordActivity
import com.example.network.dto.auth.request.LoginRequest
import com.example.network.dto.auth.response.LoginResponse
import com.example.network.service.AuthService
import com.kentreyhan.commons.utils.ToastUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    val emailValue: MutableLiveData<String?> = MutableLiveData<String?>()
    val passwordValue: MutableLiveData<String?> = MutableLiveData<String?>()

    val isLoading: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()

    private lateinit var authService: AuthService

    fun navigateToLoginPasswordActivity(context: Context) {
        print(emailValue.value)
        //if (emailValue.value.isNullOrEmpty()){
        //return
        //}
        context.startActivity(
            Intent(context, LoginPasswordActivity::class.java)
                .putExtra("E-Mail", emailValue.value)
        )
    }

    fun navigateToRegisterActivity(context: Context) {
        context.startActivity(Intent(context, RegisterActivity::class.java))
    }

    fun onEmailChanged(email: String) {
        emailValue.value = email
    }

    fun onIsLoadingChanged(value: Boolean) {
        isLoading.value = value
    }

    fun onPasswordChanged(password: String) {
        passwordValue.value = password
    }

    fun login(context: Context) {
        onIsLoadingChanged(true)

        authService = ApiServiceBuilder.getAuthInstance(context)
        if (emailValue.value.isNullOrEmpty()) {
            ToastUtils().showToast(context,"Email must be filled to be able to login")
            onIsLoadingChanged(false)
            return
        } else {

            authService.login(
                LoginRequest(
                    email = emailValue.value.toString(),
                    password = passwordValue.value.toString()
                )
            )
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("Error Login", "null1")
                        ToastUtils().showToast(context,"Login failed, please try again")
                        onIsLoadingChanged(false)
                        return
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        val loginResponse = response.body()
                        if (loginResponse == null) {
                            ToastUtils().showToast(context,"Login failed, please check your email and/or password to " +
                                    "make sure it's correct")
                            onIsLoadingChanged(false)
                            return
                        }
                        TokenUtils(context).saveToken(loginResponse.refreshToken.toString())
                        context.startActivity(
                            Intent(context, ActivitiesActivity::class.java)
                        )
                    }
                })
        }

    }

}