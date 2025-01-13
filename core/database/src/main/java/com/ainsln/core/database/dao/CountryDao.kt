package com.ainsln.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ainsln.core.database.model.entity.CountryEntity

@Dao
abstract class  CountryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    internal abstract suspend fun insert(country: CountryEntity): Long

    suspend fun getOrInsert(country: CountryEntity): Long  {
        val countryId = insert(country)
        return if (countryId == -1L) country.numeric else countryId
    }

}
