package com.abhiket.transactions.di

import android.content.Context
import com.abhiket.transactions.data.local.TokenManagerImpl
import com.abhiket.transactions.data.remote.AuthInterceptor
import com.abhiket.transactions.data.remote.AuthenticationApi
import com.abhiket.transactions.data.remote.AuthenticationRepositoryImpl
import com.abhiket.transactions.data.remote.TransactionsApi
import com.abhiket.transactions.data.remote.TransactionsRepositoryImpl
import com.abhiket.transactions.domain.AuthenticationRepository
import com.abhiket.transactions.domain.TokenManager
import com.abhiket.transactions.domain.TransactionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://api.prepstripe.com/"

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManagerImpl(context, false)
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor {
        return AuthInterceptor(tokenManager)
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }


    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }


    @Provides
    @Singleton
    fun provideAuthenticationApi(retrofitBuilder: Retrofit.Builder): AuthenticationApi {
        return retrofitBuilder
            .build()
            .create(AuthenticationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionsApi(
        okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): TransactionsApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(TransactionsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(api: AuthenticationApi): AuthenticationRepository {
        return AuthenticationRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideTransactionsRepository(api: TransactionsApi): TransactionsRepository {
        return TransactionsRepositoryImpl(api)
    }


}