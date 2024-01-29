package com.example.models.dao

interface Debts {
    suspend fun insert(
        debtId: String,
        userId: Int,
        amount: Double,
        owedToId: Int,
        groupId: Int
    )

    suspend fun check(userId: Int,owedToId: Int) : Boolean

    suspend fun update(
        userId: Int,
        owedToId: Int,
        amount: Double
    )
}