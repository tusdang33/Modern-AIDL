package com.kaizm.serviceconnector.model

import com.kaizm.serviceconnector.entity.response.UserResponse

data class UserModel(
    val userId: Int,
    val userBalance: Double
)

fun UserResponse.toUserModel() = UserModel(
    userId = userId,
    userBalance = userBalance
)