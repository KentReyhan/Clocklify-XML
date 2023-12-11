package com.kentreyhan.clocklify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kentreyhan.clocklify.login.activity.LoginEmailActivity

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, LoginEmailActivity::class.java))
    }
}