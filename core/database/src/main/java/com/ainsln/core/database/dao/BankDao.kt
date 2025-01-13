package com.ainsln.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ainsln.core.database.model.entity.BankEntity

@Dao
abstract class BankDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    internal abstract suspend fun insert(bank: BankEntity): Long

    @Query("SELECT id FROM Bank WHERE name=:name " +
            "AND (city = :city " +
            "OR (city IS NULL AND :city IS NULL))")
    internal abstract suspend fun checkBankByNameAndCity(name: String, city: String?): Long?

    suspend fun getOrInsert(bank: BankEntity): Long  {
        val existingId = checkBankByNameAndCity(bank.name, bank.city)
        return existingId ?: insert(bank)
    }

}
