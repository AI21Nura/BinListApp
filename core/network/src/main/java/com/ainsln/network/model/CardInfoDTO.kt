package com.ainsln.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardInfoDTO(
    @SerialName("number") val number: CardNumberDTO,
    @SerialName("scheme") val scheme: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("brand") val brand: String? = null,
    @SerialName("prepaid") val prepaid: Boolean? = null,
    @SerialName("country") val country: CountryDTO,
    @SerialName("bank") val bank: BankDTO
)
