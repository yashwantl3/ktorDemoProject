package entities

import kotlinx.serialization.Serializable

@Serializable
data class Amount(
    val value: Double,
    val currency: String
)