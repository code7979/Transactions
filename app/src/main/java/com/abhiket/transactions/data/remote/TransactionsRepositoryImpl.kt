package com.abhiket.transactions.data.remote

import com.abhiket.transactions.data.remote.model.Transaction
import com.abhiket.transactions.domain.TransactionsRepository
import com.abhiket.transactions.utils.getResponseOrThrows
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val transactionsApi: TransactionsApi
) : TransactionsRepository {

    override suspend fun getAppTransaction(): List<Transaction> {
        return withContext(Dispatchers.IO) {
            transactionsApi.getTransactions().getResponseOrThrows()
        }
    }
}