package com.data.datasource.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountData(entity: LocalAccountData)

    @Query("SELECT * FROM LocalAccountData")
    fun getAccountDataFlow(): Flow<LocalAccountData>

    @Query("DELETE FROM LocalAccountData")
    suspend fun deleteAccountData()
}