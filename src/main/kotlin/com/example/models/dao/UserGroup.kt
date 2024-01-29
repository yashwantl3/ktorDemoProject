package com.example.models.dao

import com.example.entities.UserGroup
import entities.User
import java.util.UUID

interface UserGroup {
    suspend fun insert(
        users: List<Int>,
    ) : UserGroup?

    suspend fun getAllGroups(): List<UserGroup>?

    suspend fun getGroupById(groupId: Int): UserGroup?
    suspend fun getAllUsersInGrp(grpId: Int): List<User?>

    suspend fun updateTransaction(groupId: Int,amt: Double): Unit
}