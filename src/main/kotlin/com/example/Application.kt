package com.example

import com.example.models.dao.Transaction
import com.example.models.dao.UserGroup
import com.example.models.dao.UserInterface
import com.example.plugins.*
import com.example.repo.DatabaseFactory
import com.example.repo.TransactionImp
import com.example.repo.UserGroupImp
import com.example.repo.UserRepo
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
        val dao : UserInterface = UserRepo()
        val txnDao: Transaction = TransactionImp()
        val grpDao: UserGroup = UserGroupImp()
        DatabaseFactory.init()
        splitWiseRouting(dao,txnDao,grpDao)
    }.start(wait = true)
}


