package com.kentreyhan.clocklify.register.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kentreyhan.clocklify.databinding.ActivityRegisterBinding
import com.kentreyhan.clocklify.register.viewmodel.RegisterViewModel

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val loginViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEvent()
        initObserver()
    }

    private fun initEvent() {
        binding.registerActionButton.setOnClickListener {
            binding.loadingIndicator.visibility = View.VISIBLE
            loginViewModel.register(this)
            binding.loadingIndicator.visibility = View.GONE
        }
        binding.registerEmailTextField.addTextChangedListener {
            loginViewModel.onEmailChanged(binding.registerEmailTextField.text)
        }
        binding.registerPasswordTextField.addTextChangedListener {
            loginViewModel.onPasswordChanged(binding.registerPasswordTextField.text)
        }
        binding.registerConfirmPasswordTextField.addTextChangedListener {
            loginViewModel.onConfirmPasswordChanged(binding.registerConfirmPasswordTextField.text)
        }
    }

    private fun initObserver() {
        loginViewModel.emailValue.observe(this) { email ->
            if(email.isNullOrEmpty()){
                binding.registerEmailTextField.errorText = "Email Invalid"
            }

        }
        loginViewModel.passwordValue.observe(this) { password ->
            if(password.isNullOrEmpty()){
                binding.registerPasswordTextField.errorText = "Password Invalid"
            }

        }
        loginViewModel.confirmPasswordValue.observe(this) { confirmPassword ->
            if(confirmPassword.isNullOrEmpty()){
                binding.registerConfirmPasswordTextField.errorText = "Confirm Password Invalid"
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