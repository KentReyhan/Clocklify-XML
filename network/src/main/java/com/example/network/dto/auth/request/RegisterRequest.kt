package com.example.network.dto.auth.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String,

    @SerializedName("confPassword")
    var confPassword: String,
)
