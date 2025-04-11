package com.abhiket.transactions.utils

import androidx.annotation.StringRes
import com.abhiket.transactions.R
import com.google.gson.Gson
import java.io.Reader
import java.util.regex.Pattern

const val EMPTY_STRING = ""
const val SHORT_DELAY = 1500L
private const val REGEX = "Bearer\\s(\\S+)"

fun getToken(string: String): String? {
    val pattern = Pattern.compile(REGEX)
    val matcher = pattern.matcher(string)
    return if (matcher.find()) matcher.group(1) else null
}

inline fun <reified T> Reader.toObjects(): T {
    return Gson().fromJson(this, T::class.java)
}

@StringRes
fun getHttpErrorMessage(httpStatusCode: Int): Int {
    return when (httpStatusCode) {
        400 -> R.string.error_http_400
        401 -> R.string.error_http_401
        403 -> R.string.error_http_403
        404 -> R.string.error_http_404
        408 -> R.string.error_http_408
        429 -> R.string.error_http_429
        500 -> R.string.error_http_500
        502 -> R.string.error_http_502
        503 -> R.string.error_http_503
        504 -> R.string.error_http_504
        else -> R.string.error_http_default
    }
}

fun handleErrorResponse() {

}