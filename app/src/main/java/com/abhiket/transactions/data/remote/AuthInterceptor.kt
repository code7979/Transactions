package com.abhiket.transactions.data.remote

import com.abhiket.transactions.domain.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.retrieve(TokenManager.KEY)
        val request = chain.request().newBuilder()
        if (token != null) {
            request.addHeader(HEADER_AUTHORIZATION, token)
        }
        return chain.proceed(request.build())
    }
}