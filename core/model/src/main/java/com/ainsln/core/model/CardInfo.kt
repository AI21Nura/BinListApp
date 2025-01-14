package com.ainsln.core.model

import java.util.Date

data class CardInfo(
    val bin: String,
    val date: Date,
    val number: CardNumber,
    val scheme: String?,
    val type: String?,
    val brand: String?,
    val prepaid: Boolean?,
    val country: Country?,
    val bank: Bank?
)
