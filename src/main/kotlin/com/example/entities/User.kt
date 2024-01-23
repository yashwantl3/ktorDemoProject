package entities

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: Int,
    val name: String,
    var balance: Double
)
