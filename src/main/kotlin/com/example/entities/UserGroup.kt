package com.example.entities

import entities.User
import kotlinx.serialization.Serializable

@Serializable
data class UserGroup(
    val users: List<User>,
    val payment: Double,
)
