package com.example.dao.token

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class TokenUtils(context: Context){

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private var encryptedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        USER_TOKEN,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    companion object {
        const val USER_TOKEN = "user_token"
    }

    fun saveToken(token: String) {
        val editor = encryptedPrefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return encryptedPrefs.getString(USER_TOKEN, null)
    }
}