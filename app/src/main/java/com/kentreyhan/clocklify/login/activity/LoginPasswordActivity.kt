package com.kentreyhan.clocklify.login.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
            if(password.isNullOrEmpty()){
                binding.passwordTextField.errorText = "Email Invalid"
            }

        }
    }
}