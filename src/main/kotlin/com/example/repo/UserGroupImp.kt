package com.example.repo

import com.example.mapper.GroupMapper
import com.example.mapper.UserMapper
import com.example.models.GroupTable
import com.example.models.UserTable
import com.example.models.dao.UserGroup
import entities.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserGroupImp: UserGroup {
    override suspend fun insert(users: List<Int>): com.example.entities.UserGroup? {
        var statement: InsertStatement<Number>?=null
        DatabaseFactory.dbQuery {
            statement=GroupTable.insert {
                it[GroupTable.users]= Json.encodeToString(users)
                it[totalTxn]=0.0
            }
        }
        return statement?.resultedValues?.get(0)?.let { GroupMapper(it) }
    }

    override suspend fun getAllGroups(): List<com.example.entities.UserGroup>? {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupById(groupId: Int): com.example.entities.UserGroup? =
        DatabaseFactory.dbQuery {
            GroupTable.select(GroupTable.groupId.eq(groupId))
                .map{
                    GroupMapper(it)
                }.singleOrNull()
        }

    override suspend fun getAllUsersInGrp(grpId: Int): List<User> {
        return DatabaseFactory.dbQuery {
            val usersString = GroupTable.select((GroupTable.groupId.eq(grpId))).map { it[GroupTable.users] }.single()
            val userIds = Json.decodeFromString<List<Int>>(usersString)
            UserTable.select((UserTable.userId inList userIds)).mapNotNull { UserMapper(it) }
        }
    }

    override suspend fun updateTransaction(groupId: Int, amt: Double) {
        DatabaseFactory.dbQuery {
            val x = GroupTable.select { GroupTable.groupId eq groupId }.single()
            val total = x[GroupTable.totalTxn]
            GroupTable.update({ GroupTable.groupId eq groupId }) {group->
             group[totalTxn] = total + amt
            }
        }
    }

}