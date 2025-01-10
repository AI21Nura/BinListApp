package com.ainsln.network.retrofit

import com.ainsln.network.BinNetworkDataSource
import com.ainsln.network.model.CardInfoDTO
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import jakarta.inject.Inject
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class BinRetrofitDataSource @Inject constructor(
    networkJson: Json,
    okHttpClient: OkHttpClient
) : BinNetworkDataSource {

    private val binListApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .build()
        .create(BinListApi::class.java)

    override suspend fun get(bin: String): Result<CardInfoDTO> = binListApi.get(bin)

    companion object {
        private const val BASE_URL = "https://lookup.binlist.net/"
    }
}
