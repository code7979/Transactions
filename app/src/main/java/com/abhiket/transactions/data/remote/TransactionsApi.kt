package com.abhiket.transactions.data.remote

import com.abhiket.transactions.data.remote.model.Transaction
import retrofit2.Call
import retrofit2.http.GET

interface TransactionsApi {
    @GET("transactions")
    fun getTransactions(): Call<List<Transaction>>
}