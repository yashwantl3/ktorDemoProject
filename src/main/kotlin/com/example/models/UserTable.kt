package com.example.models

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UserTable: Table() {
    val userId:Column<Int> = integer("userId").autoIncrement()
    val name:Column<String> = varchar("name",512)
    val balance:Column<Double> = double("balance")

    override val primaryKey: PrimaryKey = PrimaryKey(userId)
}