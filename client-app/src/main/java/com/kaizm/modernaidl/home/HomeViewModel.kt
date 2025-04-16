package com.kaizm.modernaidl.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaizm.modernaidl.CurrentUser
import com.kaizm.serviceconnector.BankServiceRepository
import com.kaizm.serviceconnector.common.fail
import com.kaizm.serviceconnector.common.success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bankServiceRepository: BankServiceRepository
) : ViewModel() {

    private val _userState = MutableStateFlow(UserUiState())
    val userState = _userState.asStateFlow()

    init {
        bankServiceRepository.connectService()
        viewModelScope.launch {
            delay(500)
            fetchBalance(CurrentUser.userId)
        }
    }

    private fun fetchBalance(userId: Int) = viewModelScope.launch {
        bankServiceRepository.fetchBalance(userId).success { user ->
            _userState.update {
                it.copy(
                    balance = user?.userBalance.toString(),
                    transactionError = null
                )
            }
        }.fail { error ->
            Log.e("Fetch Balance", "Fetch balance fail: $error")
            _userState.update {
                it.copy(
                    transactionError = error?.errorMessage
                )
            }
        }
    }

    fun deposit(amount: Double) = viewModelScope.launch {
        bankServiceRepository.deposit(CurrentUser.userId, amount).success {
            fetchBalance(CurrentUser.userId)
        }.fail { error ->
            Log.e("Deposit", "Deposit fail: $error")
            _userState.update {
                it.copy(
                    transactionError = error?.errorMessage
                )
            }
        }
    }

    fun withdraw(amount: Double) = viewModelScope.launch {
        bankServiceRepository.withdraw(CurrentUser.userId, amount).success {
            fetchBalance(CurrentUser.userId)
        }.fail { error ->
            Log.e("Withdraw", "Withdraw fail: $error")
            _userState.update {
                it.copy(
                    transactionError = error?.errorMessage
                )
            }
        }
    }
}

data class UserUiState(
    val balance: String = "0.0",
    val transactionError: String? = null
)