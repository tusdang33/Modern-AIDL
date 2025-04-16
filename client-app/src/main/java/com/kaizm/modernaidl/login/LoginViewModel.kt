package com.kaizm.modernaidl.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaizm.modernaidl.CurrentUser
import com.kaizm.serviceconnector.BankServiceRepository
import com.kaizm.serviceconnector.common.fail
import com.kaizm.serviceconnector.common.success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val bankServiceRepository: BankServiceRepository
) : ViewModel() {
    init {
        bankServiceRepository.connectService()
    }

    private val _loginResult = Channel<Boolean>(UNLIMITED)
    val loginResult = _loginResult.receiveAsFlow()

    fun login(username: String, password: String) = viewModelScope.launch {
        bankServiceRepository.login(username, password).success {
            CurrentUser.userId = it?.userId ?: -1
            _loginResult.send(true)
        }.fail { error ->
            Log.e("Login", "login fail: $error")
            _loginResult.send(false)
        }
    }
}