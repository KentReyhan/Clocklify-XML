package com.kentreyhan.clocklify.login.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.kentreyhan.clocklify.databinding.ActivityLoginPasswordBinding
import com.kentreyhan.clocklify.login.viewmodel.LoginViewModel


class LoginPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPasswordBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("E-Mail")?.let { loginViewModel.onEmailChanged(it) }
        initEvent()
        initObserver()
    }

    private fun initEvent() {
        binding.passwordActionButton.setOnClickListener {

            loginViewModel.login(this)
        }

        binding.passwordTextField.addTextChangedListener {
            loginViewModel.onPasswordChanged(binding.passwordTextField.text)
        }
    }

    private fun initObserver() {
        loginViewModel.passwordValue.observe(this) { password ->
            if (password.isNullOrBlank()) {
                binding.passwordTextField.errorText = "Password is Empty"
            }
            else{
                binding.passwordTextField.errorText = null
            }
        }

        loginViewModel.isLoading.observe(this) { loading ->
            if (loading == true) {
                binding.loadingIndicator.visibility = View.VISIBLE
            } else {
                binding.loadingIndicator.visibility = View.INVISIBLE
            }
        }
    }
}