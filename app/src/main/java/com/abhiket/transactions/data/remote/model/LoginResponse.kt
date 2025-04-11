package com.abhiket.transactions.data.remote.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success") val isSuccess: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String = ""
)