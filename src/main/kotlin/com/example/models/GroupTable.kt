package com.example.models

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object GroupTable: Table() {
    val groupId: Column<Int> = integer("groupId").autoIncrement()
    val users: Column<String> = varchar("users", 512)
    val totalTxn: Column<Double> = double("total_txn")

    override val primaryKey: PrimaryKey = PrimaryKey(GroupTable.groupId)
}
