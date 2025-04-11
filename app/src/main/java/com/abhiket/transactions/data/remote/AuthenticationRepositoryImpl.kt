package com.abhiket.transactions.data.remote

import com.abhiket.transactions.data.remote.model.Credentials
import com.abhiket.transactions.data.remote.model.LoginResponse
import com.abhiket.transactions.domain.AuthenticationRepository
import com.abhiket.transactions.utils.getResponseOrThrows
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationApi: AuthenticationApi
) : AuthenticationRepository {

    override suspend fun login(credentials: Credentials): LoginResponse {
        return withContext(Dispatchers.IO) {
            val call = authenticationApi.login(credentials)
            call.getResponseOrThrows()
        }
    }
}
