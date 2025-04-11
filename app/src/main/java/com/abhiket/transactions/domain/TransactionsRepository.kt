package com.abhiket.transactions.domain

import com.abhiket.transactions.data.remote.model.Transaction

interface TransactionsRepository {
    suspend fun getAppTransaction(): List<Transaction>
}