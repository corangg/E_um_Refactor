package com.data.repository

import android.content.Context
import com.core.di.IoDispatcher
import com.core.di.LocalDataSources
import com.core.di.RemoteDataSources
import com.data.datasource.LocalDataSource
import com.data.datasource.RemoteNaverMapDataSource
import com.data.datasource.RemoteNaverSearchDataSource
import com.data.datasource.RemoteTMapDataSource
import com.data.datasource.local.room.LocalChatData
import com.data.mapper.toExternal
import com.data.mapper.toLocal
import com.data.mapper.toLocalList
import com.data.mapper.toRemoteCar
import com.data.mapper.toRemotePublicTransport
import com.data.mapper.toRemoteWalk
import com.domain.model.AddressItemData
import com.domain.model.AddressSaveResult
import com.domain.model.ChatData
import com.domain.model.ChatMessageData
import com.domain.model.FriendItemData
import com.domain.model.ScheduleData
import com.domain.model.StartEndCoordinate
import com.domain.model.UserInfo
import com.domain.repository.Repository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DefaultRepository @Inject constructor(
    @LocalDataSources private val localDataSource: LocalDataSource,
    @RemoteDataSources private val remoteNaverMapDataSource: RemoteNaverMapDataSource,
    @RemoteDataSources private val remoteNaverSearchDataSource: RemoteNaverSearchDataSource,
    @RemoteDataSources private val remoteTMapDataSource: RemoteTMapDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : Repository {
    override suspend fun getUserInfo() = withContext(ioDispatcher) {
        localDataSource.getUserInfoData()?.toExternal()
    }

    override suspend fun upsertUserInfo(userInfo: UserInfo) = withContext(ioDispatcher) {
        localDataSource.upsertUserInfoData(userInfo.toLocal())
    }

    override fun getUserInfoFlow() = localDataSource.getUserInfoDataFlow().map { it.toExternal() }

    override fun getAddressListFlow() = localDataSource.getAddressDataListFlow().map { list -> list.map { it.toExternal() } }

    override suspend fun getAddressList() = withContext(ioDispatcher) {
        localDataSource.getAddressDataList().map { it.toExternal() }
    }

    override suspend fun upsertAddressList(addressData: AddressItemData) = withContext(ioDispatcher){
        localDataSource.upsertAddressData(addressData.toLocal())
    }

    override suspend fun deleteAddress(address: String) = withContext(ioDispatcher) {
        localDataSource.deleteAddressData(address)
    }

    override suspend fun deleteAllAddress() = withContext(ioDispatcher) {
        localDataSource.deleteAddressAllData()
    }

    override suspend fun checkAddressData(addressData: AddressItemData) = withContext(ioDispatcher) {
        val addressList = localDataSource.getAddressDataList()
        return@withContext if (addressList.any { it.address == addressData.address }) {
            AddressSaveResult.DuplicateAddress.code
        } else if (addressList.any { it.title == addressData.title }) {
            AddressSaveResult.DuplicateName.code
        } else {
            AddressSaveResult.Success.code
        }
    }

    override suspend fun updateAddressDataList(addressDataList: List<AddressItemData>) = withContext(ioDispatcher) {
        localDataSource.updateAddressDataList(addressDataList.map { it.toLocal() })
    }

    override fun getFriendListFlow() = localDataSource.getFriendDataListFlow().map { list -> list.map { it.toExternal() } }

    override suspend fun getFriendList() = withContext(ioDispatcher){
        localDataSource.getFriendDataList().map { it.toExternal()}
    }

    override suspend fun upsertFriendData(email: String, friendItemData: FriendItemData) = withContext(ioDispatcher) {
        localDataSource.upsertFriendData(friendItemData.toLocal(email))
    }

    override suspend fun deleteAllFriendData() = withContext(ioDispatcher) {
        localDataSource.deleteAllFriendData()
    }

    override suspend fun deleteFriendData(email: String) = withContext(ioDispatcher) {
        localDataSource.deleteFriendData(email)
    }

    override suspend fun initChatData(data: ChatData) = withContext(ioDispatcher) {
        localDataSource.upsertChatData(data.toLocal())
    }

    override suspend fun updateChatData(data: ChatMessageData, code: String) = withContext(ioDispatcher) {
        val chatData = localDataSource.getChatData(code)
        val newData = if (chatData != null) {
            val chatList = chatData.chatList.toMutableList()
            val newChat = data.toLocalList()
            val newChatList = (chatList + newChat).distinctBy { it.time }
            LocalChatData(chatCode = code, chatList = newChatList)
        } else {
            LocalChatData(chatCode = code, chatList = listOf(data.toLocalList()))
        }
        localDataSource.upsertChatData(newData)
    }

    override fun getChatList(code: String) = localDataSource.getChatDataFlow(code).mapNotNull { it?.toExternal() }

    override suspend fun getChat(code: String) = withContext(ioDispatcher){
        localDataSource.getChatData(code)?.toExternal()
    }

    override fun getAllChatData() = localDataSource.getAllChatData().mapNotNull { data -> data.map { it.toExternal() } }

    override suspend fun deleteChat() = withContext(ioDispatcher){
        localDataSource.deleteChatData()
    }

    override suspend fun getGeoCode(address: String) = withContext(ioDispatcher) {
        val cleanedAddress = address.trim()
            .substringBefore(",")
            .replace(Regex("\\s+"), " ")
        remoteNaverMapDataSource.getAddressToGeoCode(cleanedAddress).toExternal()
    }

    override suspend fun getReverseGeoCode(coords: String) = withContext(ioDispatcher) {
        remoteNaverMapDataSource.getReverseGeoCode(coords).toExternal()
    }

    override suspend fun getKeywordSearch(keyword: String) = withContext(ioDispatcher) {
        remoteNaverSearchDataSource.getInfoToKeyword(keyword).toExternal()
    }

    override suspend fun getPublicTransPortTime(
        coordinate: StartEndCoordinate,
        startTime: String
    ) = withContext(ioDispatcher) {
        val requestBody = coordinate.toRemotePublicTransport(startTime)
        return@withContext runCatching { remoteTMapDataSource.getPublicTransportTime(requestBody).metaData.plan.itineraries.firstOrNull()?.totalTime }.getOrNull()
    }

    override suspend fun getCarTime(coordinate: StartEndCoordinate) = withContext(ioDispatcher) {
        val requestBody = coordinate.toRemoteCar()
        return@withContext runCatching { remoteTMapDataSource.getCarTime(requestBody)?.features?.firstOrNull()?.properties?.totalTime }.getOrNull()
    }

    override suspend fun getWalkTime(coordinate: StartEndCoordinate) = withContext(ioDispatcher) {
        val requestBody = coordinate.toRemoteWalk()
        return@withContext runCatching { remoteTMapDataSource.getWalkTime(requestBody).features.firstOrNull()?.properties?.totalTime }.getOrNull()
    }

    override suspend fun upsertScheduleData(scheduleData: ScheduleData) = withContext(ioDispatcher) {
        localDataSource.upsertScheduleData(scheduleData.toLocal())
    }

    override fun getScheduleDataListFlow() = localDataSource.getScheduleDataListFlow().mapNotNull { data -> data.map { it.toExternal() } }

    override suspend fun getScheduleData(time: String) = withContext(ioDispatcher) {
        return@withContext localDataSource.getScheduleData(time)?.toExternal()
    }

    override suspend fun deleteScheduleData(time: String) = withContext(ioDispatcher) {
        localDataSource.deleteScheduleData(time)
    }

    override suspend fun updateScheduleAccept(time: String) = withContext(ioDispatcher) {
        val scheduleData = localDataSource.getScheduleData(time)?: return@withContext
        localDataSource.upsertScheduleData(scheduleData.copy(requestValue = true))
    }
}