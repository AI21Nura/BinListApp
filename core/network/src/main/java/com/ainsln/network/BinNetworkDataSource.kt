package com.ainsln.network

import com.ainsln.network.model.CardInfoDTO

interface BinNetworkDataSource {
    suspend fun get(bin: String): Result<CardInfoDTO>
}
