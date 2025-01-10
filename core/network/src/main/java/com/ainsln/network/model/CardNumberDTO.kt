package com.ainsln.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardNumberDTO(
    @SerialName("length") val length: Int? = null,
    @SerialName("luhn") val luhn: Boolean? = null
)
