package com.example.repo

import com.example.models.UserTable
import com.example.models.dao.UserInterface
import entities.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserRepo: UserInterface {
    override suspend fun insert(name: String, balance: Double): User? {
        var statement:InsertStatement<Number>?=null
        DatabaseFactory.dbQuery {
            statement = UserTable.insert { user ->
                user[UserTable.name] = name
                user[UserTable.balance] = balance
            }
        }
        return rowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun getAllUsers(): List<User>? =
        DatabaseFactory.dbQuery {
            UserTable.  selectAll().mapNotNull {
                rowToUser(it)
            }
        }


    override suspend fun getUserbyId(userId: Int): User? =
        DatabaseFactory.dbQuery {
            UserTable.select { UserTable.userId.eq(userId) }
                .map {
                    rowToUser(it)
                }.singleOrNull()
        }

    override suspend fun deleteById(userId: Int): Unit =
        DatabaseFactory.dbQuery {
            UserTable.deleteWhere { UserTable.userId.eq(userId) }
        }

    override suspend fun updateUserBalance(userId: Int, balance: Double): Unit =
        DatabaseFactory.dbQuery {
            UserTable.update({UserTable.userId.eq(userId)}){user->
                user[UserTable.balance] = balance
            }
        }


    private fun rowToUser(row: ResultRow?): User? {
        if (row == null)
            return null
        return User(
            name = row[UserTable.name],
            balance = row[UserTable.balance],
            userId = row[UserTable.userId]
        )
    }
}