package com.ainsln.binlist.di

import android.content.Context
import com.ainsln.core.common.manager.AssetManager
import com.ainsln.core.common.manager.DefaultAssetManager
import com.ainsln.core.common.utils.AndroidLogcatLogger
import com.ainsln.core.common.utils.AppDispatchers
import com.ainsln.core.common.utils.Logger
import com.ainsln.core.data.utils.BaseExceptionHandler
import com.ainsln.core.data.utils.DateTimeProvider
import com.ainsln.core.data.utils.ExceptionHandler
import com.ainsln.core.data.utils.SystemDateTimeProvider
import com.ainsln.core.ui.utils.BaseIntentSender
import com.ainsln.core.ui.utils.IntentSender
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilsModule {

    @Binds
    @Singleton
    abstract fun bindsAssetManager(
        manager: DefaultAssetManager
    ) : AssetManager

    @Binds
    @Singleton
    abstract fun bindsLogger(
        logger: AndroidLogcatLogger
    ) : Logger

    @Binds
    @Singleton
    abstract fun bindsExceptionHandler(
        handler: BaseExceptionHandler
    ) : ExceptionHandler

    @Binds
    @Singleton
    abstract fun bindsDateTimeProvider(
        provider: SystemDateTimeProvider
    ) : DateTimeProvider

    companion object {
        @Provides
        @Singleton
        fun provideAppDispatchers(): AppDispatchers = AppDispatchers()

        @Provides
        fun providesIntentSender(
            @ApplicationContext context: Context
        ): IntentSender = BaseIntentSender(context)
    }

}
