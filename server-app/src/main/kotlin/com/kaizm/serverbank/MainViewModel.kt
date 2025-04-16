package com.kaizm.serverbank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _clientStatus = Channel<String>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val clientStatus: Flow<String> = _clientStatus.receiveAsFlow()

    private val _timer = MutableStateFlow(0)
    val timer: StateFlow<Int> = _timer

    private var timerJob: Job? = null

    fun updateStatus(msg: String) {
        _clientStatus.trySend(msg)
        val concatMsg = msg.split(" ")
        if (concatMsg.any { it.equals("connected", true) }) {
            startTimer()
        } else if (concatMsg.any { it.equals("disconnected", true) }) {
            stopTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        _timer.value = 0

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _timer.value += 1
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}