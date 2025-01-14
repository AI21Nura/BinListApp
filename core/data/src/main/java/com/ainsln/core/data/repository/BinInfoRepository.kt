package com.ainsln.core.data.repository

import com.ainsln.core.common.result.Result
import com.ainsln.core.model.CardInfo
import kotlinx.coroutines.flow.Flow

interface BinInfoRepository {

    fun getByBin(bin: String): Flow<Result<CardInfo>>

    fun getAll(): Flow<Result<List<CardInfo>>>

    suspend fun clearAll()
}
