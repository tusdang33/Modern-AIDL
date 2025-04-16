package com.kaizm.serviceconnector.di

import android.content.Context
import com.kaizm.serviceconnector.BankServiceRepository
import com.kaizm.serviceconnector.BankServiceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    fun provideBankServiceRepository(@ApplicationContext context: Context): BankServiceRepository {
        return BankServiceRepositoryImpl(context)
    }
}