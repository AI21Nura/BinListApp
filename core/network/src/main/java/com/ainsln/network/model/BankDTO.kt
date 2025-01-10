package com.ainsln.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BankDTO(
    @SerialName("name") val name: String? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("phone") val phone: String? = null,
    @SerialName("city") val city: String? = null
)
