package com.abhiket.transactions.domain

import com.abhiket.transactions.R
import com.abhiket.transactions.data.remote.model.ErrorResponse
import com.abhiket.transactions.data.remote.model.Transaction
import com.abhiket.transactions.utils.getHttpErrorMessage
import com.abhiket.transactions.utils.toObjects
import com.admin.rhythmbox.utils.StringValue
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TransitionsUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {

    suspend operator fun invoke(): Task<List<Transaction>> {
        return try {
            val result: List<Transaction> = repository.getAppTransaction()
            Task.Success(result)
        } catch (e: IOException) {
            Task.Failure(StringValue.StringResource(R.string.error_login_io_exception))
        } catch (e: JsonSyntaxException) {
            Task.Failure(StringValue.StringResource(R.string.error_login_io_exception))
        } catch (e: KotlinNullPointerException) {
            Task.Failure(StringValue.StringResource(R.string.error_null_response))
        } catch (e: HttpException) {
            val response = e.response()
            val stringValue = if (response != null) {
                val errorBody = response.errorBody()
                if (errorBody != null) {
                    val errorResponse = errorBody.charStream().toObjects<ErrorResponse>()
                    StringValue.DynamicString(errorResponse.message)
                } else {
                    StringValue.StringResource(getHttpErrorMessage(e.code()))
                }
            } else {
                StringValue.StringResource(getHttpErrorMessage(e.code()))
            }
            Task.Failure(stringValue)
        }
    }

}
