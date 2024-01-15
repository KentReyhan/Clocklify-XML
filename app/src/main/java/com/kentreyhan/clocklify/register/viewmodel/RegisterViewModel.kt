package com.kentreyhan.clocklify.register.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dao.token.TokenUtils
import com.example.network.api.ApiServiceBuilder
import com.example.network.dto.auth.request.LoginRequest
import com.example.network.dto.auth.request.RegisterRequest
import com.example.network.dto.auth.response.LoginResponse
import com.example.network.dto.auth.response.RegisterResponse
import com.kentreyhan.clocklify.activities.activity.ActivitiesActivity
import com.kentreyhan.commons.utils.ToastUtils
import com.kentreyhan.commons.widgets.dialog.CustomDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    val emailValue: MutableLiveData<String?> = MutableLiveData<String?>()
    val passwordValue: MutableLiveData<String?> = MutableLiveData<String?>()
    val confirmPasswordValue: MutableLiveData<String?> = MutableLiveData<String?>()

    val isLoading: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()

    fun onEmailChanged(email: String) {
        emailValue.value = email
    }

    fun onPasswordChanged(password: String) {
        passwordValue.value = password
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        confirmPasswordValue.value = confirmPassword
    }

    fun onIsLoadingChanged(value: Boolean) {
        isLoading.value = value
    }

    fun register(context: Context) {
        onIsLoadingChanged(true)

        if (emailValue.value.isNullOrEmpty() || passwordValue.value.isNullOrEmpty() || confirmPasswordValue.value.isNullOrEmpty()) {
            onIsLoadingChanged(false)
            ToastUtils().showToast(context,"Please fill out all the field")
            return
        }
        val authService = ApiServiceBuilder.getAuthInstance(context)

        authService.register(
            RegisterRequest(
                email = emailValue.value.toString(),
                password = passwordValue.value.toString(),
                confPassword = confirmPasswordValue.value.toString()
            )
        )
            .enqueue(object : Callback<RegisterResponse> {
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Log.d("Error Login", "null1")
                    ToastUtils().showToast(context,"Register failed, please try again")
                    onIsLoadingChanged(false)
                    return
                }

                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    val loginResponse = response.body()
                    if (loginResponse == null) {
                        ToastUtils().showToast(context,"Register failed, please check your email and/or password to " +
                                "make sure it's correct")
                        onIsLoadingChanged(false)
                        return
                    }
                    val dialog = CustomDialog(context,"Success","Your account has been successfully created.")
                    dialog.apply {
                        show()
                    }
                }
            })

    }
}