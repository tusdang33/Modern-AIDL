package com.kaizm.serverbank.di

import android.content.Context
import androidx.room.Room
import com.kaizm.serverbank.DatabaseConst
import com.kaizm.serverbank.data.repository.implement.BankRepositoryImpl
import com.kaizm.serverbank.data.repository.repositories.BankDAO
import com.kaizm.serverbank.data.repository.repositories.BankDatabase
import com.kaizm.serverbank.data.repository.repositories.BankRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideBankDatabase(@ApplicationContext context: Context): BankDatabase {
        return Room.databaseBuilder(
            context,
            BankDatabase::class.java,
            DatabaseConst.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideBankDAO(bankDatabase: BankDatabase): BankDAO {
        return bankDatabase.downloadTaskDAO
    }

    @Singleton
    @Provides
    fun provideBankRepository(bankDAO: BankDAO): BankRepository {
        return BankRepositoryImpl(bankDAO)
    }
}