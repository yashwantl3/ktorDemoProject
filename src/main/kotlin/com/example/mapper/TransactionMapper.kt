package com.example.mapper

import com.example.models.TransactionTable
import entities.Amount
import entities.Transaction
import entities.User
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow

fun TransactionMapper(row: ResultRow?) : Transaction?{
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
