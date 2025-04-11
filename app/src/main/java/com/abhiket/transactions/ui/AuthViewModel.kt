package com.abhiket.transactions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhiket.transactions.R
import com.abhiket.transactions.data.remote.model.LoginResponse
import com.abhiket.transactions.domain.TokenManager
import com.abhiket.transactions.domain.LoginWithUsername
import com.abhiket.transactions.domain.Task
import com.admin.rhythmbox.utils.StringValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCase: LoginWithUsername,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState = MutableLiveData<UiState<String>>()
    val loginState: LiveData<UiState<String>> get() = _loginState

    fun loginWithUsername(username: String, password: String, isUserAgreementTicked: Boolean) {
        viewModelScope.launch {
            if (!isUserAgreementTicked) {
                _loginState.postValue(UiState.Failure(StringValue.StringResource(R.string.error_user_agreement_required)))
            } else if (username.isBlank()) {
                _loginState.postValue(UiState.Failure(StringValue.StringResource(R.string.error_login_empty_username)))
            } else if (password.isBlank()) {
                _loginState.postValue(UiState.Failure(StringValue.StringResource(R.string.error_login_empty_password)))
            } else {
                _loginState.postValue(UiState.Loading)
                when (val response = useCase.invoke(username, password)) {
                    is Task.Success -> {
                        val loginResponse: LoginResponse = response.data
                        _loginState.postValue(UiState.Success(loginResponse.message))
                        tokenManager.store(
                            key = TokenManager.KEY,
                            value = loginResponse.token
                        )
                    }

                    is Task.Failure -> {
                        _loginState.postValue(UiState.Failure(response.message))
                    }
                }
            }
        }
    }
}