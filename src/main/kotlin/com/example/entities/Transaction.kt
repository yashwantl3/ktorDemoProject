package entities

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Transaction(
    val txnId: Int,
    val users: List<User>,
    val payee: User,
    val amt: Amount,
    val description: String?,
    val createdOn: String?
)
