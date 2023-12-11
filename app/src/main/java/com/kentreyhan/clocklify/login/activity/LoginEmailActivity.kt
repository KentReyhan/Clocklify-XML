package com.kentreyhan.clocklify.login.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.kentreyhan.clocklify.databinding.ActivityLoginEmailBinding
import com.kentreyhan.clocklify.login.viewmodel.LoginViewModel

class LoginEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginEmailBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEvent()
        initObserver()
    }

    private fun initEvent() {
        binding.registerTextButton.setOnClickListener {
            loginViewModel.navigateToRegisterActivity(this)
        }
        binding.emailActionButton.setOnClickListener {
            loginViewModel.navigateToLoginPasswordActivity(this)
        }

        binding.emailTextField.addTextChangedListener {
            loginViewModel.onEmailChanged(binding.emailTextField.text.toString())
        }
    }

    private fun initObserver() {
        loginViewModel.emailValue.observe(this) { email ->
            if(email.isNullOrEmpty()){
                binding.errorTextEmail.text = "Email Invalid"
                binding.errorTextEmail.visibility = View.VISIBLE
            }
            else{
                binding.errorTextEmail.visibility = View.GONE
            }
        }
    }
}