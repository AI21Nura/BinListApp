package com.ainsln.core.database

import com.ainsln.core.database.model.entity.BankEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BankDaoTest : BaseDaoTest() {

    @Test
    fun insertBank_successfullyInsertsAndReturnsId() = runTest {
        val bank = createBankEntity()
        val id = bankDao.insert(bank)
        assertEquals(id, 1)
    }

    @Test
    fun checkBankByNameAndCity_findsMatchingBank() = runTest {
        val bank = createBankEntity()
        val id = bankDao.insert(bank)

        val foundId = bankDao.checkBankByNameAndCity(name = "Test Bank", city = "Test City")
        assertEquals(id, foundId)
    }

    @Test
    fun checkBankByNameAndCity_returnsNullForNonExistingBank() = runTest {
        val foundId = bankDao.checkBankByNameAndCity(name = "Nonexistent Bank", city = "Nonexistent City")
        assertNull(foundId)
    }

    @Test
    fun getOrInsert_insertsNewBankIfNotExists() = runTest {
        val bank = createBankEntity()
        val id = bankDao.getOrInsert(bank)

        assertEquals(1, id)
    }

    @Test
    fun getOrInsert_returnsExistingIdIfBankExists() = runTest {
        val bank = createBankEntity()
        val id = bankDao.getOrInsert(bank)
        val secondId = bankDao.getOrInsert(bank)

        assertEquals(id, secondId)
    }

    @Test
    fun insertBank_withNullCity_storesCorrectly() = runTest {
        val bank = BankEntity(id = 0, name = "Null City Bank", url = null, phone = null, city = null)
        val id = bankDao.insert(bank)
        assertEquals(1, id)

        val foundId = bankDao.checkBankByNameAndCity(name = "Null City Bank", city = null)
        assertEquals(id, foundId)
    }
}
