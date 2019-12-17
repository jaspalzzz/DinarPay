package com.ssas.tpcms.android.data

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message")
    var message: String,
    @SerializedName("statusCode")
    var statusCode: String
)