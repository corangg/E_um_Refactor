package com.data.datasource.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfoData(entity: LocalUserInfoData)

    @Query("SELECT * FROM LocalUserInfoData")
    fun getUserInfoData(): LocalUserInfoData?

    @Query("SELECT * FROM LocalUserInfoData")
    fun getUserInfoDataFlow(): Flow<LocalUserInfoData>

    @Upsert
    suspend fun upsertUserInfoData(entity: LocalUserInfoData)

    @Query("DELETE FROM LocalUserInfoData")
    suspend fun deleteUserInfoData()
}