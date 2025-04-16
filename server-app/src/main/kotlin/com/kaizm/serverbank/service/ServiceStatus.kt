package com.kaizm.serverbank.service

import kotlinx.coroutines.flow.MutableSharedFlow

object ServiceStatus {
    var onStatusUpdate: ((String) -> Unit)? = null
}