package com.kaizm.serverbank.data.repository.repositories

import com.kaizm.serverbank.common.Resource
import com.kaizm.serviceconnector.entity.response.AuthResponse
import com.kaizm.serviceconnector.entity.response.UserResponse

interface BankRepository {
    suspend fun checkBalance(userId: Int): Resource<UserResponse>
    suspend fun login(userName: String, password: String): Resource<AuthResponse>
    suspend fun signup(userName: String, password: String): Resource<Unit>
    suspend fun deposit(userId: Int, amount: Double): Resource<Unit>
    suspend fun withdraw(userId: Int, amount: Double): Resource<Unit>
}