package com.data.datasource

import com.data.datasource.local.room.LocalUserInfoData
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertUserInfoData(entity: LocalUserInfoData)
    suspend fun getUserInfoData(): LocalUserInfoData?
    fun getUserInfoDataFlow(): Flow<LocalUserInfoData>
    suspend fun upsertUserInfoData(entity: LocalUserInfoData)
    suspend fun deleteUserInfoData()
}