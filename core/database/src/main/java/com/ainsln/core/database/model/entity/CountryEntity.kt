package com.ainsln.core.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Country")
data class CountryEntity(
    @PrimaryKey val numeric: Long,
    val alpha2: String?,
    val name: String?,
    val emoji: String?,
    val currency: String?,
    val latitude: Double?,
    val longitude: Double?
)
