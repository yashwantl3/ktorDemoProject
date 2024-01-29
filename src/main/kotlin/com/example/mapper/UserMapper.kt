package com.example.mapper

import com.example.entities.UserGroup
import com.example.models.GroupTable
import com.example.models.UserTable
import entities.User
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow

fun UserMapper(row: ResultRow) : User? {
    if (row == null)
        return null
    return User(
        name = row[UserTable.name],
        balance = row[UserTable.balance],
        userId = row[UserTable.userId]
    )
}