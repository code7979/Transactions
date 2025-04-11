package com.abhiket.transactions.domain

import com.abhiket.transactions.R
import com.abhiket.transactions.data.remote.model.Credentials
import com.abhiket.transactions.data.remote.model.LoginResponse
import com.abhiket.transactions.utils.getHttpErrorMessage
import com.abhiket.transactions.utils.toObjects
import com.admin.rhythmbox.utils.StringValue
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginWithUsername @Inject constructor(private val repository: AuthenticationRepository) {

    suspend operator fun invoke(username: String, password: String): Task<LoginResponse> {
        return try {
            val result: LoginResponse = repository.login(Credentials(username, password))
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
                    val errorResponse = errorBody.charStream().toObjects<LoginResponse>()
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
