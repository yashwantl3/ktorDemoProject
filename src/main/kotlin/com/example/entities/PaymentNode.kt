package com.example.entities

import entities.Amount

data class PaymentNode(
    val payerUid: Int,
    val payeeUid: Int,
    val amt: Amount
)
