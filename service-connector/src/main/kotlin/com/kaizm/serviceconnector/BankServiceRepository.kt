package com.kaizm.serviceconnector

import com.kaizm.serviceconnector.common.Resource
import com.kaizm.serviceconnector.model.AuthModel
import com.kaizm.serviceconnector.model.FailModel
import com.kaizm.serviceconnector.model.UserModel

interface BankServiceRepository {
    fun connectService()
    fun disconnectService()
    suspend fun login(userName: String, password: String): Resource<AuthModel, FailModel>
    suspend fun signup(userName: String, password: String): Resource<Boolean, FailModel>
    suspend fun fetchBalance(userId: Int): Resource<UserModel, FailModel>
    suspend fun deposit(userId: Int, amount: Double): Resource<Boolean, FailModel>
    suspend fun withdraw(userId: Int, amount: Double): Resource<Boolean, FailModel>
}