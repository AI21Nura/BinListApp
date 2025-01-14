package com.ainsln.core.data.repository

import android.database.sqlite.SQLiteException
import com.ainsln.core.common.result.AppException
import com.ainsln.core.common.result.Result
import com.ainsln.core.data.utils.DateTimeProvider
import com.ainsln.core.data.utils.ExceptionHandler
import com.ainsln.core.data.utils.toBankEntity
import com.ainsln.core.data.utils.toCardInfoModel
import com.ainsln.core.data.utils.toCountryEntity
import com.ainsln.core.database.dao.BankDao
import com.ainsln.core.database.dao.CardInfoDao
import com.ainsln.core.database.dao.CountryDao
import com.ainsln.core.database.model.FullCardInfo
import com.ainsln.core.database.model.entity.BankEntity
import com.ainsln.core.database.model.entity.CardInfoEntity
import com.ainsln.core.database.model.entity.CountryEntity
import com.ainsln.core.database.room.BinDatabaseWrapper
import com.ainsln.network.BinNetworkDataSource
import com.ainsln.network.model.BankDTO
import com.ainsln.network.model.CardInfoDTO
import com.ainsln.network.model.CardNumberDTO
import com.ainsln.network.model.CountryDTO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException
import java.util.Date

class DefaultBinInfoRepositoryTest {
    @Mock
    private lateinit var networkDataSource: BinNetworkDataSource
    @Mock
    private lateinit var database: BinDatabaseWrapper
    @Mock
    private lateinit var exceptionHandler: ExceptionHandler
    @Mock
    private lateinit var cardInfoDao: CardInfoDao
    @Mock
    private lateinit var bankDao: BankDao
    @Mock
    private lateinit var countryDao: CountryDao

    private lateinit var repository: DefaultBinInfoRepository

    private val dateTimeProvider = DateTimeProvider { Date(100) }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        whenever(database.cardInfoDao()).thenReturn(cardInfoDao)
        whenever(database.bankDao()).thenReturn(bankDao)
        whenever(database.countryDao()).thenReturn(countryDao)

        repository = DefaultBinInfoRepository(
            networkDataSource = networkDataSource,
            database = database,
            exceptionHandler = exceptionHandler,
            dateTimeProvider = dateTimeProvider
        )
    }


    @Test
    fun `getByBin emits Loading then Success when network call succeeds`() = runTest {
        // Arrange
        val bin = "123456"
        val cardInfoDTO = createTestCardInfoDTO()
        val expectedCardInfo = cardInfoDTO.toCardInfoModel(bin, dateTimeProvider)

        whenever(networkDataSource.get(bin))
            .thenReturn(kotlin.Result.success(cardInfoDTO))

        // Act
        val results = repository.getByBin(bin).toList()

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Success)
        assertEquals(expectedCardInfo, (results[1] as Result.Success).data)
    }

    @Test
    fun `getByBin emits Loading then Error when network call fails`() = runTest {
        // Arrange
        val bin = "123456"
        val networkException = IOException("Network error")
        val appException = AppException.NetworkError(0, "Network error occurred")

        whenever(networkDataSource.get(bin))
            .thenReturn(kotlin.Result.failure(networkException))
        whenever(exceptionHandler.handle(networkException))
            .thenReturn(appException)

        // Act
        val results = repository.getByBin(bin).toList()

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Error)
        assertEquals(appException, (results[1] as Result.Error).e)
    }

    @Test
    fun `getByBin saves data to database when network call succeeds`() = runTest {
        // Arrange
        val bin = "123456"
        val cardInfoDTO = createTestCardInfoDTO()
        val bankEntity = cardInfoDTO.bank?.toBankEntity()
        val countryEntity = cardInfoDTO.country?.toCountryEntity()

        whenever(networkDataSource.get(bin))
            .thenReturn(kotlin.Result.success(cardInfoDTO))
        whenever(bankDao.getOrInsert(bankEntity!!))
            .thenReturn(1L)
        whenever(countryDao.getOrInsert(countryEntity!!))
            .thenReturn(840L)

        // Act
        repository.getByBin(bin).toList()

        // Verify
        verify(bankDao).getOrInsert(bankEntity)
        verify(countryDao).getOrInsert(countryEntity)
        verify(cardInfoDao).upsert(any())
    }

    @Test
    fun `getAll emits Loading then Success with empty list when database is empty`() = runTest {
        // Arrange
        whenever(cardInfoDao.getAll())
            .thenReturn(flowOf(emptyList()))

        // Act
        val results = repository.getAll().toList()

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Success)
        assertTrue((results[1] as Result.Success).data.isEmpty())
    }

    @Test
    fun `getAll emits Loading then Success with data when database has records`() = runTest {
        // Arrange
        val fullCardInfo = createTestFullCardInfo()
        val cardInfo = fullCardInfo.toCardInfoModel()
        whenever(cardInfoDao.getAll())
            .thenReturn(flowOf(listOf(fullCardInfo)))

        // Act
        val results = repository.getAll().toList()

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Success)
        assertEquals(1, (results[1] as Result.Success).data.size)
        assertEquals(cardInfo, (results[1] as Result.Success).data[0])
    }

    @Test
    fun `getAll emits Loading then Error when database throws exception`() = runTest {
        // Arrange
        val dbException = SQLiteException()
        val appException = AppException.DatabaseError(null)

        whenever(cardInfoDao.getAll())
            .thenReturn(flow { throw dbException })
        whenever(exceptionHandler.handle(dbException))
            .thenReturn(appException)

        // Act
        val results = repository.getAll().toList()

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is Result.Loading)
        assertTrue(results[1] is Result.Error)
        assertEquals(appException, (results[1] as Result.Error).e)
    }

    @Test
    fun `clearAll successfully clears database`() = runTest {
        // Act
        repository.clearAll()

        // Verify
        verify(database).clearAll()
    }

    @Test
    fun `getByBin skips saving bank when bank name is null`() = runTest {
        // Arrange
        val bin = "123456"
        val cardInfoDTO = createTestCardInfoDTO().copy(
            bank = BankDTO(name = null, url = "test.com", phone = "123", city = "Test")
        )

        whenever(networkDataSource.get(bin))
            .thenReturn(kotlin.Result.success(cardInfoDTO))

        // Act
        repository.getByBin(bin).toList()

        // Verify
        verify(bankDao, never()).getOrInsert(any())
    }

    @Test
    fun `getByBin skips saving country when numeric is null`() = runTest {
        // Arrange
        val bin = "123456"
        val cardInfoDTO = createTestCardInfoDTO().copy(
            country = CountryDTO(numeric = null, alpha2 = "US", name = "USA",
                emoji = "🇺🇸", currency = "USD",
                latitude = 38.0, longitude = -97.0)
        )

        whenever(networkDataSource.get(bin))
            .thenReturn(kotlin.Result.success(cardInfoDTO))

        // Act
        repository.getByBin(bin).toList()

        // Verify
        verify(countryDao, never()).getOrInsert(any())
    }

    private fun createTestCardInfoDTO() = CardInfoDTO(
        number = CardNumberDTO(length = 16, luhn = true),
        scheme = "visa",
        type = "debit",
        brand = "Visa Classic",
        prepaid = false,
        country = CountryDTO(
            numeric = "840",
            alpha2 = "US",
            name = "United States",
            emoji = "🇺🇸",
            currency = "USD",
            latitude = 38.0,
            longitude = -97.0
        ),
        bank = BankDTO(
            name = "JPMorgan Chase",
            url = "www.chase.com",
            phone = "+1-123-456-7890",
            city = "New York"
        )
    )

    private fun createTestFullCardInfo() = FullCardInfo(
        cardInfo = CardInfoEntity(
            bin = "123456",
            date = Date(),
            numberLength = 16,
            numberLuhn = true,
            scheme = "visa",
            type = "debit",
            brand = "Visa Classic",
            prepaid = false,
            countryId = 1L,
            bankId = 1L
        ),
        country = CountryEntity(
            numeric = 840L,
            alpha2 = "US",
            name = "United States",
            emoji = "🇺🇸",
            currency = "USD",
            latitude = 38.0,
            longitude = -97.0
        ),
        bank = BankEntity(
            id = 1L,
            name = "JPMorgan Chase",
            url = "www.chase.com",
            phone = "+1-123-456-7890",
            city = "New York"
        )
    )
}
