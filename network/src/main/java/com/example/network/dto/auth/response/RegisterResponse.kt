package com.example.network.dto.auth.response

import com.example.network.user.User
import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("msg")
    var msg: String,
)
