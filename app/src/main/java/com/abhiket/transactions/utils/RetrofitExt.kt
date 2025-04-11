package com.abhiket.transactions.utils

import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Invocation

fun <T : Any> Call<T>.getResponseOrThrows(): T {
    val response = execute()
    if (response.isSuccessful) {
        val body = response.body()
        if (body != null) {
            return body
        } else {
            val invocation = request().tag(Invocation::class.java)!!
            val service = invocation.service()
            val method = invocation.method()
            throw KotlinNullPointerException("Response from ${service.name}.${method.name} was null but response body type was declared as non-null")
        }
    } else {
        throw HttpException(response)
    }
}