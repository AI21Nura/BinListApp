package com.ainsln.network.fake

import com.ainsln.core.common.manager.AssetManager
import com.ainsln.core.common.utils.AppDispatchers
import com.ainsln.network.BinNetworkDataSource
import com.ainsln.network.createNotFoundException
import com.ainsln.network.model.CardInfoDTO
import jakarta.inject.Inject
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
    private val dispatchers: AppDispatchers
) : BinNetworkDataSource {

    override suspend fun get(bin: String): Result<CardInfoDTO> =
        withContext(dispatchers.io) {
            try {
                val binInd = existingBins.indexOf(bin)
                if (binInd == -1) throw createNotFoundException()
                Result.success(getAll()[binInd])
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }

    @OptIn(ExperimentalSerializationApi::class)
    private fun getAll(): List<CardInfoDTO> {
        return assetManager.open("bincard.json")
            .use { networkJson.decodeFromStream(it) }
    }

    companion object {
        private val existingBins = listOf(
            "111111",
            "222222",
            "33333333",
        )
    }
}
