package com.example

import com.example.di.DaggerSplitWiseDataComponent
import com.example.di.SplitWiseModule
import com.example.models.dao.Debts
import com.example.models.dao.Transaction
import com.example.models.dao.UserGroup
import com.example.models.dao.UserInterface
import com.example.plugins.splitWiseRouting
import com.example.repo.DatabaseFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0"){
        install(ContentNegotiation){
            json()
        }
//        configureRouting()
//        contactUsModule()


        val application: Application = this
        val component = DaggerSplitWiseDataComponent.builder()
            .splitWiseModule(SplitWiseModule(application))
            .build()
        component.inject(this)

        val dao: UserInterface = component.provideUserDao()
        val txnDao: Transaction = component.provideTransactionDao()
        val grpDao: UserGroup = component.provideGroupDao()
        val debtsDao: Debts = component.provideDebtsDao()

        DatabaseFactory.init()
        splitWiseRouting(dao, txnDao, grpDao, debtsDao)
    }.start(wait = true)
}


