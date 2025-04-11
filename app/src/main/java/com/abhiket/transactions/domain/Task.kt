package com.abhiket.transactions.domain

import com.admin.rhythmbox.utils.StringValue

sealed interface Task<out T> {
    data class Success<out T>(val data: T) : Task<T>
    data class Failure(val message: StringValue) : Task<Nothing>
}