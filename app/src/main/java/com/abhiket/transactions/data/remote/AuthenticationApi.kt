package com.abhiket.transactions.data.remote

import com.abhiket.transactions.data.remote.model.Credentials
import com.abhiket.transactions.data.remote.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {
    @POST("login")
    fun login(@Body credentials: Credentials): Call<LoginResponse>
}