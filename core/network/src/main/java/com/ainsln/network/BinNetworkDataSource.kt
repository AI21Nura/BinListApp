package com.ainsln.network

import com.ainsln.network.model.CardInfoDTO
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

interface BinNetworkDataSource {
    suspend fun get(bin: String): Result<CardInfoDTO>

    companion object {
        internal const val NOT_FOUND_CODE = 404
        internal const val NOT_FOUND_MESSAGE = "No matching cards are found"
    }
}

internal fun createNotFoundException(): HttpException {
    return HttpException(
        Response.error<Any>(
            BinNetworkDataSource.NOT_FOUND_CODE,
            BinNetworkDataSource.NOT_FOUND_MESSAGE.toResponseBody(null)
        )
    )
}
