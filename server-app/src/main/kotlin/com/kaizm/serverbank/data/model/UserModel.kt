package com.kaizm.serverbank.data.model

import com.kaizm.serverbank.data.entity.UserAccount
import com.kaizm.serviceconnector.entity.response.AuthResponse
import com.kaizm.serviceconnector.entity.response.UserResponse


fun UserAccount.toUserResponse(): UserResponse = UserResponse(
    userId = userId,
    userBalance = balance
)

fun UserAccount.toAuthResponse(): AuthResponse = AuthResponse(
    userId = userId,
    userName = username
)