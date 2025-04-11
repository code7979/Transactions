package com.abhiket.transactions.data.remote.model

data class Transaction(
    val id: Long,
    val date: String,
    val amount: Long,
    val category: String,
    val description: String,
)