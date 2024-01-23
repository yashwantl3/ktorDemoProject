package com.example.models.dao

import entities.Amount
import entities.Transaction
import entities.User


interface Transaction {
    suspend fun insert(
        txnId: Int,
        users: List<User>,
        payee: User,
        amt: Amount,
        description: String?,
        createdOn: String?
    ): entities.Transaction?

    suspend fun getAllTxn():List<entities.Transaction>?

    suspend fun getTxnById(txnId:Int): Transaction?
}