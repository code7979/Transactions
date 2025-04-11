package com.abhiket.transactions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhiket.transactions.data.remote.model.Transaction
import com.abhiket.transactions.domain.Task
import com.abhiket.transactions.domain.TokenManager
import com.abhiket.transactions.domain.TransitionsUseCase
import com.abhiket.transactions.utils.SHORT_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransitionsViewModel @Inject constructor(
    private val useCase: TransitionsUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _transitions = MutableLiveData<UiState<List<Transaction>>>()
    val transitions: LiveData<UiState<List<Transaction>>> get() = _transitions

    private val _isLogout = MutableLiveData<Boolean>()
    val isLogout: LiveData<Boolean> get() = _isLogout

    init {
        viewModelScope.launch {
            _transitions.postValue(UiState.Loading)
            when (val response = useCase()) {
                is Task.Success -> {
                    _transitions.postValue(UiState.Success(response.data))
                }

                is Task.Failure -> {
                    _transitions.postValue(UiState.Failure(response.message))
                }
            }
        }
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.clear()
            _isLogout.postValue(tokenManager.retrieve(TokenManager.KEY) == null)
        }
    }
}