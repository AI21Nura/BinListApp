package com.ainsln.network.retrofit

import com.ainsln.network.model.CardInfoDTO
import retrofit2.http.GET
import retrofit2.http.Path

internal interface BinListApi{
    @GET("{bin}")
    suspend fun get(@Path("bin") bin: String): Result<CardInfoDTO>
}
