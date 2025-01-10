package com.ainsln.network.fake

import com.ainsln.core.common.manager.AssetManager
import com.ainsln.network.BinNetworkDataSource
import com.ainsln.network.model.CardInfoDTO
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

/**
 * A fake implementation of the data source that uses local JSON assets to return data.
 * Used to avoid exceeding the request limit (5 per hour) on the real server.
 */
class BinFakeDataSource @Inject constructor(
    private val networkJson: Json,
    private val assetManager: AssetManager,
    private val dispatcher: CoroutineDispatcher
) : BinNetworkDataSource {

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun get(bin: String): Result<CardInfoDTO> = withContext(dispatcher) {
        try {
            Result.success(
                assetManager.open("bincard.json").use { networkJson.decodeFromStream(it) }
            )
        } catch (e: Throwable){
            Result.failure(e)
        }

    }
}
