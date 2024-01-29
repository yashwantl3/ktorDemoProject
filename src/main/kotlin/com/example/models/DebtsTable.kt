package com.example.models

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ForeignKeyConstraint
import org.jetbrains.exposed.sql.Table

object DebtsTable: Table() {
    val groupId: Column<Int> = integer("groupId")
    val debtId: Column<String> = varchar("debtId",512)
    val userId: Column<Int> = integer("userId")
    val amount: Column<Double> = double("amount")
    val owedToId: Column<Int> = integer("owedToId").references(UserTable.userId)

    override val primaryKey = PrimaryKey(DebtsTable.debtId)
}
