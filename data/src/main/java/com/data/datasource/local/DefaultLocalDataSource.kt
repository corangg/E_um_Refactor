package com.data.datasource.local

import com.data.datasource.LocalDataSource
import com.data.datasource.local.room.AddressDao
import com.data.datasource.local.room.ChatDao
import com.data.datasource.local.room.FriendDao
import com.data.datasource.local.room.LocalAddressItemData
import com.data.datasource.local.room.LocalChatData
import com.data.datasource.local.room.LocalFriendData
import com.data.datasource.local.room.LocalScheduleData
import com.data.datasource.local.room.LocalUserInfoData
import com.data.datasource.local.room.ScheduleDao
import com.data.datasource.local.room.UserInfoDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocalDataSource @Inject constructor(
    private val userInfoDao: UserInfoDao,
    private val addressDao: AddressDao,
    private val friendDao: FriendDao,
    private val chatData: ChatDao,
    private val scheduleDao: ScheduleDao
) : LocalDataSource {
    override suspend fun insertUserInfoData(entity: LocalUserInfoData) = userInfoDao.insertUserInfoData(entity)
    override suspend fun getUserInfoData() = userInfoDao.getUserInfoData()
    override fun getUserInfoDataFlow() = userInfoDao.getUserInfoDataFlow()
    override suspend fun upsertUserInfoData(entity: LocalUserInfoData) = userInfoDao.upsertUserInfoData(entity)
    override suspend fun deleteUserInfoData() = userInfoDao.deleteUserInfoData()

    override suspend fun upsertAddressData(entity: LocalAddressItemData) = addressDao.upsertAddressData(entity)
    override suspend fun updateAddressDataList(entityList: List<LocalAddressItemData>) = addressDao.updateAddressDataList(entityList)
    override suspend fun getAddressDataList() = addressDao.getUserInfoData()
    override fun getAddressDataListFlow() = addressDao.getUserInfoDataFlow()
    override suspend fun deleteAddressData(address: String) = addressDao.deleteAddressData(address)
    override suspend fun deleteAddressAllData() = addressDao.deleteAddressAllData()

    override suspend fun upsertFriendData(entity: LocalFriendData) = friendDao.upsertFriendData(entity)
    override suspend fun updateFriendDataList(entityList: List<LocalFriendData>) = friendDao.updateFriendDataList(entityList)
    override suspend fun getFriendDataList() = friendDao.getFriendDataList()
    override fun getFriendDataListFlow() = friendDao.getFriendDataFlow()
    override suspend fun deleteFriendData(email: String) = friendDao.deleteFriendData(email)
    override suspend fun deleteAllFriendData() = friendDao.deleteFriendAllData()

    override suspend fun upsertChatData(entity: LocalChatData) = chatData.upsertChatData(entity)
    override fun getAllChatData(): Flow<List<LocalChatData>> = chatData.getAllChatData()
    override suspend fun getChatData(code: String) = chatData.getChatData(code)
    override fun getChatDataFlow(code: String) = chatData.getChatDataFlow(code)
    override suspend fun deleteChatData() = chatData.deleteChatAllData()

    override suspend fun upsertScheduleData(entity: LocalScheduleData) = scheduleDao.upsertScheduleData(entity)
    override fun getScheduleDataListFlow(): Flow<List<LocalScheduleData>> = scheduleDao.getScheduleDataListFlow()
    override suspend fun getScheduleData(time: String): LocalScheduleData? = scheduleDao.getScheduleData(time)
    override suspend fun deleteScheduleData(time: String) = scheduleDao.deleteScheduleData(time)
}
