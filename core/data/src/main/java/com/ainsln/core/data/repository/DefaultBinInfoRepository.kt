package com.ainsln.core.data.repository

import com.ainsln.core.common.result.Result
import com.ainsln.core.common.result.asFlowResult
import com.ainsln.core.data.utils.DateTimeProvider
import com.ainsln.core.data.utils.ExceptionHandler
import com.ainsln.core.data.utils.toBankEntity
import com.ainsln.core.data.utils.toCardInfoEntity
import com.ainsln.core.data.utils.toCardInfoModel
import com.ainsln.core.data.utils.toCountryEntity
import com.ainsln.core.database.room.BinDatabaseWrapper
import com.ainsln.core.model.CardInfo
import com.ainsln.network.BinNetworkDataSource
import com.ainsln.network.model.CardInfoDTO
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DefaultBinInfoRepository @Inject constructor(
    private val networkDataSource: BinNetworkDataSource,
    private val database: BinDatabaseWrapper,
    private val exceptionHandler: ExceptionHandler,
    private val dateTimeProvider: DateTimeProvider
) : BinInfoRepository {

    override fun getByBin(bin: String): Flow<Result<CardInfo>> = flow {
        networkDataSource.get(bin)
            .onSuccess { data ->
                emit(data.toCardInfoModel(bin, dateTimeProvider))
                saveToDb(bin, data)
            }
            .onFailure { e -> throw e }
    }.asFlowResult(exceptionHandler::handle)

    override fun getAll(): Flow<Result<List<CardInfo>>> {
        return database.cardInfoDao().getAll()
            .map { list -> list.map { it.toCardInfoModel() } }
            .asFlowResult(exceptionHandler::handle)
    }

    override suspend fun clearAll() {
        database.clearAll()
    }

    /**
     * Bank is not saved if it's null or its name is null
     * Country is not saved if it's null or its numeric is null
     * Checks uniqueness of bank (name + city) and country (numeric) before insertion
     */
    private suspend fun saveToDb(bin: String, data: CardInfoDTO) {
        try {
            val bankId = data.bank?.toBankEntity()?.let { database.bankDao().getOrInsert(it) }
            val countryId =
                data.country?.toCountryEntity()?.let { database.countryDao().getOrInsert(it) }
            database.cardInfoDao().upsert(data.toCardInfoEntity(bin, bankId, countryId))
        } catch (e: Throwable) {
            exceptionHandler.handle(e)
        }
    }
}
