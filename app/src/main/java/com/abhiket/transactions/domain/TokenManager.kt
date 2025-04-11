package com.abhiket.transactions.domain

interface TokenManager {
    fun store(key: String, value: String)
    fun retrieve(key: String): String?
    fun clear()

    companion object {
        const val FILE_NAME = "com.abhiket.transactions_login_token"
        const val KEY = "com.abhiket.transactions_password"
    }
}