package com.ainsln.core.database.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "CardInfo",
    foreignKeys = [
        ForeignKey(
            entity = CountryEntity::class,
            parentColumns = ["numeric"],
            childColumns = ["country_id"]
        ),
        ForeignKey(
            entity = BankEntity::class,
            parentColumns = ["id"],
            childColumns = ["bank_id"],
            onUpdate = ForeignKey.NO_ACTION
        ),
    ],
    indices = [
        Index(value = ["country_id"]),
        Index(value = ["bank_id"])
    ]
)
data class CardInfoEntity(
    @PrimaryKey val bin: String,
    val date: Date,
    @ColumnInfo("number_length") val numberLength: Int?,
    @ColumnInfo("number_luhn") val numberLuhn: Boolean?,
    val scheme: String?,
    val type: String?,
    val brand: String?,
    val prepaid: Boolean?,
    @ColumnInfo("country_id") val countryId: Long?,
    @ColumnInfo("bank_id") val bankId: Long?
)
