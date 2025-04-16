package com.kaizm.modernaidl.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class SignupViewModel @Inject constructor(
    private val bankServiceRepository: BankServiceRepository
) : ViewModel() {
    init {
        bankServiceRepository.connectService()
    }

    private val _signupResult = Channel<Boolean>(UNLIMITED)
    val signupResult = _signupResult.receiveAsFlow()

    fun signUp(username: String, password: String) = viewModelScope.launch {
        bankServiceRepository.signup(username, password).success {
            _signupResult.send(true)
        }.fail { error ->
            Log.e("Sign Up", "Sign up fail: $error")
            _signupResult.send(false)
        }
    }
}