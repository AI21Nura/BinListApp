package com.ainsln.core.model

data class Country(
    val numeric: Long,
    val alpha2: String?,
    val name: String?,
    val emoji: String?,
    val currency: String?,
    val latitude: Double?,
    val longitude: Double?
)
