package com.ainsln.core.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ainsln.core.database.model.entity.BankEntity
import com.ainsln.core.database.model.entity.CardInfoEntity
import com.ainsln.core.database.model.entity.CountryEntity
import com.ainsln.core.database.room.BinRoomDatabase
import org.junit.After
import org.junit.Before
import java.util.Date

abstract class BaseDaoTest {
    private lateinit var database: BinRoomDatabase
    internal val bankDao by lazy { database.bankDao() }
    internal val countryDao by lazy { database.countryDao() }
    internal val cardInfoDao by lazy { database.cardInfoDao() }


    @Before
    fun setupDatabase(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BinRoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }


    @After
    fun closeDb() = database.close()

    protected fun createCardInfoEntity(): CardInfoEntity {
        return CardInfoEntity(
            bin = "123456",
            date = Date(),
            numberLength = 16,
            numberLuhn = true,
            scheme = "Visa",
            type = "Credit",
            brand = "Gold",
            prepaid = false,
            countryId = null,
            bankId = null
        )
    }

    protected fun createCountryEntity(): CountryEntity {
        return CountryEntity(
            numeric = 840,
            alpha2 = "US",
            name = "United States",
            emoji = "🇺🇸",
            currency = "USD",
            latitude = 38.0,
            longitude = -97.0
        )
    }

    protected fun createBankEntity(): BankEntity {
        return BankEntity(
            id = 0,
            name = "Test Bank",
            url = "testbank.com",
            phone = "123456789",
            city = "Test City"
        )
    }

}
