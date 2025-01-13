package com.ainsln.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.ainsln.core.database.model.entity.BankEntity
import com.ainsln.core.database.model.entity.CardInfoEntity
import com.ainsln.core.database.model.entity.CountryEntity

data class FullCardInfo(
    @Embedded val cardInfo: CardInfoEntity,
    @Relation(
        parentColumn = "country_id",
        entityColumn = "numeric"
    )
    val country: CountryEntity?,
    @Relation(
        parentColumn = "bank_id",
        entityColumn = "id"
    )
    val bank: BankEntity?,
)
