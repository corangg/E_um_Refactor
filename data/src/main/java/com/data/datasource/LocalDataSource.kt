package com.data.datasource

import com.data.datasource.local.room.LocalAddressItemData
import com.data.datasource.local.room.LocalChatData
import com.data.datasource.local.room.LocalFriendData
import com.data.datasource.local.room.LocalScheduleData
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

    suspend fun upsertChatData(entity: LocalChatData)
    fun getAllChatData(): Flow<List<LocalChatData>>
    suspend fun getChatData(code: String): LocalChatData?
    fun getChatDataFlow(code: String): Flow<LocalChatData?>
    suspend fun deleteChatData()

    suspend fun upsertScheduleData(entity: LocalScheduleData)
    fun getScheduleDataListFlow(): Flow<List<LocalScheduleData>>
    suspend fun getScheduleData(time: String): LocalScheduleData?
    suspend fun deleteScheduleData(time: String)
}