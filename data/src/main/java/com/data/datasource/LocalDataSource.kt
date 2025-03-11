package com.data.datasource

import com.data.datasource.local.room.LocalAddressItemData
import com.data.datasource.local.room.LocalFriendData
import com.data.datasource.local.room.LocalUserInfoData
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertUserInfoData(entity: LocalUserInfoData)
    suspend fun getUserInfoData(): LocalUserInfoData?
    fun getUserInfoDataFlow(): Flow<LocalUserInfoData>
    suspend fun upsertUserInfoData(entity: LocalUserInfoData)
    suspend fun deleteUserInfoData()

    suspend fun upsertAddressData(entity: LocalAddressItemData)
    suspend fun updateAddressDataList(entityList: List<LocalAddressItemData>)
    suspend fun getAddressDataList(): List<LocalAddressItemData>
    fun getAddressDataListFlow(): Flow<List<LocalAddressItemData>>
    suspend fun deleteAddressData(address: String)
    suspend fun deleteAddressAllData()

    suspend fun upsertFriendData(entity: LocalFriendData)
    suspend fun updateFriendDataList(entityList: List<LocalFriendData>)
    suspend fun getFriendDataList(): List<LocalFriendData>
    fun getFriendDataListFlow(): Flow<List<LocalFriendData>>
    suspend fun deleteFriendData(email: String)
    suspend fun deleteAllFriendData()
}