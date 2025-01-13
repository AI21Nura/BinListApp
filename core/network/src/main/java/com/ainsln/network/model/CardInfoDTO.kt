package com.ainsln.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardInfoDTO(
    @SerialName("number") val number: CardNumberDTO? = null,
    @SerialName("scheme") val scheme: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("brand") val brand: String? = null,
    @SerialName("prepaid") val prepaid: Boolean? = null,
    @SerialName("country") val country: CountryDTO? = null,
    @SerialName("bank") val bank: BankDTO? = null
)

/**
 * According to the documentation, a 404 error code should be returned when no data is found.
 * But sometimes (or always) server returns JSON with the number field is null when no data is found.
 * So check number and throw an exception with a 404 code if it's null.
 */
fun CardInfoDTO.isNotFound(): Boolean {
    return number == null
}
