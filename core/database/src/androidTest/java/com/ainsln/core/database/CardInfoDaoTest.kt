package com.ainsln.core.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CardInfoDaoTest : BaseDaoTest() {

    @Test
    fun upsert_insertsNewCardInfo() = runBlocking {
        val cardInfo = createCardInfoEntity()
        cardInfoDao.upsert(cardInfo)

        val allCards = cardInfoDao.getAll().first()
        assertEquals(1, allCards.size)
        assertEquals(cardInfo.bin, allCards[0].cardInfo.bin)
    }

    @Test
    fun upsert_updatesExistingCardInfo() = runBlocking {
        val cardInfo = createCardInfoEntity()
        cardInfoDao.upsert(cardInfo)

        val updatedCardInfo = cardInfo.copy(numberLength = 20)
        cardInfoDao.upsert(updatedCardInfo)

        val allCards = cardInfoDao.getAll().first()
        assertEquals(1, allCards.size)
        assertEquals(20, allCards[0].cardInfo.numberLength)
    }

    @Test
    fun getAll_returnsFullCardInfoWithRelations() = runBlocking {
        val countryId = countryDao.getOrInsert(createCountryEntity())
        val bankId = bankDao.getOrInsert(createBankEntity())

        val cardInfo = createCardInfoEntity().copy(
            countryId = countryId,
            bankId = bankId
        )
        cardInfoDao.upsert(cardInfo)

        val allCards = cardInfoDao.getAll().first()
        assertEquals(1, allCards.size)

        val fullCardInfo = allCards[0]
        assertNotNull(fullCardInfo.country)
        assertEquals(countryId, fullCardInfo.country?.numeric)

        assertNotNull(fullCardInfo.bank)
        assertEquals(bankId, fullCardInfo.bank?.id)
    }

    @Test
    fun getAll_returnsEmptyListWhenNoData() = runBlocking {
        val allCards = cardInfoDao.getAll().first()
        assertTrue(allCards.isEmpty())
    }
}
