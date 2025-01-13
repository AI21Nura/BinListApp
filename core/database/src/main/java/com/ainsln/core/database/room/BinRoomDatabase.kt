package com.ainsln.core.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ainsln.core.common.utils.AppDispatchers
import com.ainsln.core.database.dao.BankDao
import com.ainsln.core.database.dao.CardInfoDao
import com.ainsln.core.database.dao.CountryDao
import com.ainsln.core.database.model.entity.BankEntity
import com.ainsln.core.database.model.entity.CardInfoEntity
import com.ainsln.core.database.model.entity.CountryEntity
import com.ainsln.core.database.utils.DateConverter
import kotlinx.coroutines.withContext

@Database(
    entities = [
        BankEntity::class,
        CountryEntity::class,
        CardInfoEntity::class
               ],
    version = 1
)
@TypeConverters(DateConverter::class)
internal abstract class BinRoomDatabase : RoomDatabase() {
    abstract fun bankDao(): BankDao
    abstract fun countryDao(): CountryDao
    abstract fun cardInfoDao(): CardInfoDao
}

class BinDatabaseWrapper(
    context: Context,
    private val dispatchers: AppDispatchers
) {

    private val database = Room.databaseBuilder(
        context,
        BinRoomDatabase::class.java,
        "app.db"
    )
        .build()

    fun bankDao() = database.bankDao()
    fun countryDao() = database.countryDao()
    fun cardInfoDao() = database.cardInfoDao()
    suspend fun clearAll() = withContext(dispatchers.io) {
        database.clearAllTables()
    }
}

