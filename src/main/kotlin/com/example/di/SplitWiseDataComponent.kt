package com.example.di

import com.example.models.dao.Debts
import com.example.models.dao.Transaction
import com.example.models.dao.UserGroup
import com.example.models.dao.UserInterface
import dagger.Component
import io.ktor.server.application.*
import javax.inject.Singleton

@Singleton
@Component(modules = [SplitWiseModule::class])
interface SplitWiseDataComponent {
    fun inject(application: Application)
    fun provideUserDao(): UserInterface
    fun provideTransactionDao(): Transaction
    fun provideGroupDao(): UserGroup
    fun provideDebtsDao(): Debts
}