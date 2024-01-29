package com.example.repo

import com.example.mapper.TransactionMapper
import com.example.models.TransactionTable
import com.example.models.UserTable
import com.example.models.dao.Transaction
import entities.Amount
import entities.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement

class TransactionImp: Transaction {
    override suspend fun insert(
        txnId: String,
        users: List<User?>,
        payee: User,
        amt: Amount,
        description: String?,
        createdOn: String?
    ): entities.Transaction? {
        var statement: InsertStatement<Number>?=null
        DatabaseFactory.dbQuery {
            statement=TransactionTable.insert{ transaction ->
                transaction[TransactionTable.txnId]=txnId
                transaction[TransactionTable.payeeId]=payee.userId
                transaction[TransactionTable.payeeName]=payee.name
                transaction[TransactionTable.payeeBalance]=payee.balance
                transaction[TransactionTable.users]= Json.encodeToString(users)
                transaction[TransactionTable.value]=amt.value
                transaction[TransactionTable.currency]=amt.currency
                transaction[TransactionTable.description]=description?: "Null"
                transaction[TransactionTable.createdOn]=createdOn?: "Now"
            }
        }
        return rowToTxn(statement?.resultedValues?.get(0))
    }

    override suspend fun getAllTxn(): List<entities.Transaction>? =
        DatabaseFactory.dbQuery {
            TransactionTable.selectAll().mapNotNull {
                TransactionMapper(it)
            }
        }


    override suspend fun getTxnById(txnId: String): entities.Transaction? =
        DatabaseFactory.dbQuery {
            TransactionTable.select(TransactionTable.txnId.eq(txnId))
                .map{
                    TransactionMapper(it)
                }.singleOrNull()
        }


    private fun rowToTxn(row: ResultRow?): entities.Transaction? {
        if (row == null)
            return null
        return entities.Transaction(
            txnId = row[TransactionTable.txnId],
            amt = Amount(
                value = row[TransactionTable.value],
                currency = row[TransactionTable.currency]
            ),
            payee = User(
                userId = row[TransactionTable.payeeId],
                name = row[TransactionTable.payeeName],
                balance = row[TransactionTable.payeeBalance]
            ),
            users = Json.decodeFromString<List<User>>(row[TransactionTable.users]),
            description = row[TransactionTable.description],
            createdOn= row[TransactionTable.createdOn]
        )
    }
}

private fun parseUsersList(usersData: Any): List<User> {
    if (usersData !is List<*>) return emptyList()

    return usersData.mapNotNull { userRow ->
        if (userRow !is ResultRow) return@mapNotNull null
        User(
            userId = userRow[UserTable.userId],
            name = userRow[UserTable.name],
            balance = userRow[UserTable.balance]
        )
    }
}

