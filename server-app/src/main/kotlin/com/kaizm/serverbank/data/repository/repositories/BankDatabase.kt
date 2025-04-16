package com.kaizm.serverbank.data.repository.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kaizm.serverbank.data.entity.UserAccount

@Database(entities = [UserAccount::class], version = 1)
abstract class BankDatabase : RoomDatabase() {
    abstract val downloadTaskDAO: BankDAO
}