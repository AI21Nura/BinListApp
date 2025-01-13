package com.ainsln.core.database

import com.ainsln.core.database.model.entity.CountryEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CountryDaoTest : BaseDaoTest() {

    @Test
    fun insertCountry_successfullyInsertsAndReturnsId() = runBlocking {
        val country = createCountryEntity()
        val id = countryDao.insert(country)

        assertEquals(840, id)
    }

    @Test
    fun getOrInsert_insertsNewCountryIfNotExists() = runBlocking {
        val country = createCountryEntity()
        val id = countryDao.getOrInsert(country)

        assertEquals(840, id)
    }

    @Test
    fun getOrInsert_returnsExistingIdIfCountryExists() = runBlocking {
        val country = createCountryEntity()
        val id = countryDao.getOrInsert(country)
        val secondId = countryDao.getOrInsert(country)

        assertEquals(id, secondId)
    }

    @Test
    fun insertCountry_withPartialData_storesCorrectly() = runBlocking {
        val country = CountryEntity(numeric = 2, alpha2 = null, name = "Unknown", emoji = null, currency = null, latitude = null, longitude = null)
        val id = countryDao.insert(country)

        assertEquals(2, id)
    }


}
