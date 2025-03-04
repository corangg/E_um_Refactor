package com.data.datasource

import com.data.datasource.local.room.LocalAccountData
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertAccountData(entity: LocalAccountData)
    fun getAccountData(): Flow<LocalAccountData>
    suspend fun deleteAccountData()
}