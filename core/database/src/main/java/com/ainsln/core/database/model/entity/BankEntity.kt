package com.ainsln.core.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Bank")
data class BankEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val url: String?,
    val phone: String?,
    val city: String?
)
