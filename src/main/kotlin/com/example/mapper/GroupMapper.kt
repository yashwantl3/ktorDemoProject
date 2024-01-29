package com.example.mapper

import com.example.entities.UserGroup
import com.example.models.GroupTable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow

fun GroupMapper(row: ResultRow) : UserGroup? {
    if (row == null)
        return null
    return UserGroup(
        groupId = row[GroupTable.groupId],
        users = Json.decodeFromString<List<Int>>(row[GroupTable.users]),
        totalTxn = row[GroupTable.totalTxn]
    )
}
