package com.example.entities

data class Debt(
    var debtId: String,
    var userId: Int,
    var amount: Double,
    var owedToId: Int,
    var groupId: Int,
)