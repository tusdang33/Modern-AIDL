package com.kaizm.serviceconnector.model

import com.kaizm.serviceconnector.entity.response.AuthResponse

data class AuthModel(
    val userId: Int,
    val userName: String
)

fun AuthResponse.toAuthModel() = AuthModel(
    userId = userId,
    userName = userName
)