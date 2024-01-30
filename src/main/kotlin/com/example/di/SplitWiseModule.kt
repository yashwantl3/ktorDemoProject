package com.example.di

import com.example.models.dao.Debts
import com.example.models.dao.Transaction
import com.example.models.dao.UserGroup
import com.example.models.dao.UserInterface
import com.example.repo.DebtsImp
import com.example.repo.TransactionImp
import com.example.repo.UserGroupImp
import com.example.repo.UserRepo
import dagger.Provides
import io.ktor.server.application.*

@dagger.Module
class SplitWiseModule(private val application: Application) {
    @Provides
    fun provideUserDao(): UserInterface = UserRepo()

    @Provides
    fun provideTransactionDao() : Transaction = TransactionImp()

    @Provides
    fun provideGroupDao(): UserGroup = UserGroupImp()

    @Provides
    fun provideDebtsDao(): Debts = DebtsImp()

    @Provides
    fun provideApplication(): Application {
        return application
    }
}
