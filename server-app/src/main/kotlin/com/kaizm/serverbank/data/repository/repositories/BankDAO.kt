package com.kaizm.serverbank.data.repository.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kaizm.serverbank.DatabaseConst
import com.kaizm.serverbank.data.entity.UserAccount

@Dao
interface BankDAO {
    @Query("SELECT userId FROM ${DatabaseConst.DATABASE_TABLE_NAME} WHERE username = :username LIMIT 1")
    suspend fun getUserIdByUsername(username: String): Int?

    @Query("select * from ${DatabaseConst.DATABASE_TABLE_NAME} where username = :username AND password = :password")
    suspend fun getUserAccount(username: String, password: String): UserAccount?

    @Query("select * from ${DatabaseConst.DATABASE_TABLE_NAME} where userId = :userId")
    suspend fun getUserAccount(userId: Int): UserAccount?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserBalance(userAccount: UserAccount)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserAccount(userAccount: UserAccount)
}