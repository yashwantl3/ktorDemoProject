package com.example.mapper

import com.example.entities.Debt
import com.example.models.DebtsTable
import org.jetbrains.exposed.sql.ResultRow

fun DebtsMapper(row:ResultRow) : Debt? {
    if(row==null)
        return null
    return Debt(
        userId = row[DebtsTable.userId],
        debtId = row[DebtsTable.debtId],
        amount = row[DebtsTable.amount],
        owedToId = row[DebtsTable.owedToId],
        groupId= row[DebtsTable.groupId]
    )
}