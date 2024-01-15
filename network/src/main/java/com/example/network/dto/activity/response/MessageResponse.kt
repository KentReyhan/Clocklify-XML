package com.example.network.dto.activity.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("msg")
    var msg: String
)
