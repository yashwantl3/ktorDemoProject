package com.example.entities

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Expense(
    val expenseId: Int,
    val expenseTxn: Map<Int,Double>,
    val groupId: Int,
    val isSettled: Boolean,
    @Contextual
    val timestamp: Instant
)