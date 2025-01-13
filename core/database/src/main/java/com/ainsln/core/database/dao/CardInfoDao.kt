package com.ainsln.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ainsln.core.database.model.FullCardInfo
import com.ainsln.core.database.model.entity.CardInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardInfoDao {

    @Transaction
    @Query("SELECT * FROM CardInfo ORDER BY date DESC")
    fun getAll(): Flow<List<FullCardInfo>>

    @Upsert
    suspend fun upsert(cardInfo: CardInfoEntity)

}
