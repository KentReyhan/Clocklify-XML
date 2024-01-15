package com.example.network.user

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id")
    var id: String,

    @SerializedName("ame")
    var name: String,

    @SerializedName("email")
    var email: String
)
