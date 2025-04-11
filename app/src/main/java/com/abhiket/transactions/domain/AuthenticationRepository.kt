package com.abhiket.transactions.domain

import com.abhiket.transactions.data.remote.model.Credentials
import com.abhiket.transactions.data.remote.model.LoginResponse

interface AuthenticationRepository {
    suspend fun login(credentials: Credentials): LoginResponse
}