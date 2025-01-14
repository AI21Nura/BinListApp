package com.ainsln.binlist.di

import com.ainsln.network.BinNetworkDataSource
import com.ainsln.network.retrofit.BinRetrofitDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindsNetworkDataSource(
        datasource: BinRetrofitDataSource
    ): BinNetworkDataSource

    companion object {
        @Provides
        @Singleton
        fun providesNetworkJson(): Json = Json {
            ignoreUnknownKeys = true
        }

        @Provides
        @Singleton
        fun providesOkHttpClick(): OkHttpClient = OkHttpClient()
    }
}
