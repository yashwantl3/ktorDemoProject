package com.example.entities

import kotlinx.serialization.Serializable

@Serializable
data class UserGroup(
    val groupId: Int,
    val users: List<Int>,
    val totalTxn: Double
)
