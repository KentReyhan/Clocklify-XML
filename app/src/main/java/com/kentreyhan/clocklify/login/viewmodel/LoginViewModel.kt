package com.kentreyhan.clocklify.login.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kentreyhan.clocklify.register.activity.RegisterActivity
import com.kentreyhan.clocklify.login.activity.LoginPasswordActivity

class LoginViewModel: ViewModel() {
    val emailValue: MutableLiveData<String?> = MutableLiveData<String?>()
    val passwordValue: MutableLiveData<String?> = MutableLiveData<String?>()

    fun navigateToLoginPasswordActivity(context: Context) {
        print(emailValue.value)
        if (emailValue.value.isNullOrEmpty()){
            return
        }
        context.startActivity(
            Intent(context, LoginPasswordActivity::class.java)
            .putExtra("E-Mail",emailValue.value)
        )
    }

    fun navigateToRegisterActivity(context: Context) {
        context.startActivity(Intent(context, RegisterActivity::class.java))
    }

    fun onEmailChanged(email: String) {
        emailValue.value = email
    }


    fun onPasswordChanged(password: String) {
        passwordValue.value = password
    }

    fun login(context: Context) {
        if(emailValue.value.isNullOrEmpty()){
            Log.d("Email", "null")
            return
        }
        else{
            //TODO:Temporary, will be directed to timer page
            Log.d("Email", emailValue.value!!)
        }
    }

}