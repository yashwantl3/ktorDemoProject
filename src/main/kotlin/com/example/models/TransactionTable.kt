package com.example.models

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object TransactionTable:Table() {
    val txnId: Column<String> = varchar("txnId",512)
    val payeeId: Column<Int> = integer("payee_Id",)
    val payeeName: Column<String> = varchar("payee_name", 512)
    val payeeBalance: Column<Double> = double("payee_balance",)
    val users: Column<String> = varchar("users", 512)
    val value: Column<Double> = double("amt_val")
    val currency: Column<String> = varchar("currency",20)
    val description: Column<String> = varchar("description", 512)
    val createdOn: Column<String> = varchar("Created On", 100)

    override val primaryKey: PrimaryKey = PrimaryKey(TransactionTable.txnId)
}