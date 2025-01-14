package com.ainsln.binlist.di

import android.content.Context
import com.ainsln.core.common.utils.AppDispatchers
import com.ainsln.core.database.room.BinDatabaseWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context,
        dispatchers: AppDispatchers
    ) : BinDatabaseWrapper {
        return BinDatabaseWrapper(context, dispatchers)
    }

}
