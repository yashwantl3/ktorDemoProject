package entities

import java.util.*

data class UserShare(
    val txnId: Int,
    val userId: UUID,
    val share: Amount,
    val category: Category
)
