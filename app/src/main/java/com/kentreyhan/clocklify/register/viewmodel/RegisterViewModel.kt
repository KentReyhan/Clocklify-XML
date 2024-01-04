package com.kentreyhan.clocklify.register.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kentreyhan.widget.dialog.CustomDialog

class RegisterViewModel : ViewModel() {
    val emailValue: MutableLiveData<String?> = MutableLiveData<String?>()
    val passwordValue: MutableLiveData<String?> = MutableLiveData<String?>()
    val confirmPasswordValue: MutableLiveData<String?> = MutableLiveData<String?>()

    fun register(context: Context) {
        if (emailValue.value.isNullOrEmpty() || passwordValue.value.isNullOrEmpty() || confirmPasswordValue.value.isNullOrEmpty()) {
            return
        }
        val dialog = CustomDialog(context,"Success","Your account has been successfully created.")
        dialog.apply {
            show()
        }
    }

    fun onEmailChanged(email: String) {
        emailValue.value = email
    }

    fun onPasswordChanged(password: String) {
        passwordValue.value = password
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        confirmPasswordValue.value = confirmPassword
    }
}