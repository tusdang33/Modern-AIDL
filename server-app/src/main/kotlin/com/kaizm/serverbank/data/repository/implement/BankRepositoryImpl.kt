package com.kaizm.serverbank.data.repository.implement

import com.kaizm.serverbank.common.Resource
import com.kaizm.serverbank.data.entity.UserAccount
import com.kaizm.serverbank.data.model.toAuthResponse
import com.kaizm.serverbank.data.model.toUserResponse
import com.kaizm.serverbank.data.repository.repositories.BankDAO
import com.kaizm.serverbank.data.repository.repositories.BankRepository
import com.kaizm.serviceconnector.entity.response.AuthResponse
import com.kaizm.serviceconnector.entity.response.UserResponse
import javax.inject.Inject

class BankRepositoryImpl @Inject constructor(
    private val bankDAO: BankDAO
) : BankRepository {
    override suspend fun checkBalance(userId: Int): Resource<UserResponse> {
        return try {
            Resource.Success(bankDAO.getUserAccount(userId)?.toUserResponse())
        } catch (e: Exception) {
            Resource.Fail(e.message)
        }
    }

    override suspend fun login(userName: String, password: String): Resource<AuthResponse> {
        return try {
            val userAccount = bankDAO.getUserAccount(userName, password)
            if (userAccount != null)
                Resource.Success(userAccount.toAuthResponse()) else Resource.Fail("User not found")
        } catch (e: Exception) {
            Resource.Fail(e.message)
        }
    }

    override suspend fun signup(userName: String, password: String): Resource<Unit> {
        return try {
            if (bankDAO.getUserIdByUsername(userName) != null) return Resource.Fail("User already exists")
            Resource.Success(
                bankDAO.saveUserAccount(
                    UserAccount(
                        username = userName,
                        password = password
                    )
                )
            )
        } catch (e: Exception) {
            Resource.Fail(e.message)
        }
    }

    override suspend fun deposit(userId: Int, amount: Double): Resource<Unit> {
        return try {
            val userAccount =
                bankDAO.getUserAccount(userId) ?: return Resource.Fail("User not found")
            userAccount.balance += amount
            bankDAO.updateUserBalance(userAccount)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Fail(e.message)
        }
    }

    override suspend fun withdraw(userId: Int, amount: Double): Resource<Unit> {
        return try {
            val userAccount =
                bankDAO.getUserAccount(userId) ?: return Resource.Fail("User not found")
            if (userAccount.balance < amount) return Resource.Fail("Insufficient balance")
            userAccount.balance -= amount
            bankDAO.updateUserBalance(userAccount)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Fail(e.message)
        }
    }
}