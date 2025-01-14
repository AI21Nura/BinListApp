package com.ainsln.binlist.di

import com.ainsln.core.data.repository.BinInfoRepository
import com.ainsln.core.data.repository.DefaultBinInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsBinInfoRepository(
        repository: DefaultBinInfoRepository
    ) : BinInfoRepository

}
