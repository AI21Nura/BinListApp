package com.ainsln.binlist.di

import com.ainsln.core.common.manager.AssetManager
import com.ainsln.core.common.manager.DefaultAssetManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilsModule {

    @Binds
    @Singleton
    abstract fun bindsAssetManager(
        manager: DefaultAssetManager
    ) : AssetManager

    companion object {
        @Provides
        fun provideCoroutineDispatchers(): CoroutineDispatcher = Dispatchers.IO
    }

}
