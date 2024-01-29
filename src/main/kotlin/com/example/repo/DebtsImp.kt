package com.example.repo

import com.example.models.DebtsTable
import com.example.models.TransactionTable
import com.example.models.UserTable
import com.example.models.dao.Debts
import entities.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DebtsImp: Debts{
    override suspend fun insert(debtId: String, userId: Int, amount: Double, owedToId: Int,groupId: Int) {
        DatabaseFactory.dbQuery {
            DebtsTable.insert{
                it[DebtsTable.debtId]=debtId
                it[DebtsTable.userId]=userId
                it[DebtsTable.amount]=amount
                it[DebtsTable.owedToId]=owedToId
                it[DebtsTable.groupId]=groupId
            }
        }
    }

    override suspend fun check(userId: Int,owedToId: Int): Boolean{
        var res: ResultRow?=null
        DatabaseFactory.dbQuery {
            res = DebtsTable.select {
                DebtsTable.userId.eq(userId) and DebtsTable.owedToId.eq(owedToId)
            }.singleOrNull()
        }
        println("Check query status: $res")
        return res!=null   //If not null, means there is a entry -> returns True
    }

    override suspend fun update(userId: Int, owedToId: Int,amount: Double) {
        DatabaseFactory.dbQuery {
            val x= DebtsTable.select { DebtsTable.userId.eq(userId) and DebtsTable.owedToId.eq(owedToId) }.single()
            val prev=x[DebtsTable.amount]
            DebtsTable.update({DebtsTable.userId.eq(userId) and DebtsTable.owedToId.eq(owedToId)}){
                it[DebtsTable.amount]=prev+amount
            }
        }
    }

}