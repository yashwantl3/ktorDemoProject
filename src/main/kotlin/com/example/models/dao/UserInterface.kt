package com.example.models.dao

import entities.User

interface UserInterface {
    suspend fun insert(
        name: String,
        balance: Double
    ) : User?

    suspend fun getAllUsers():List<User>?

    suspend fun getUserbyId(userId:Int): User?

    suspend fun deleteById(userId: Int): Unit

    suspend fun updateUserBalance(userId: Int,balance: Double)
}