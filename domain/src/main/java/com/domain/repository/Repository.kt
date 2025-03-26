package com.domain.repository

import com.domain.model.AddressItemData
import com.domain.model.ChatData
import com.domain.model.ChatMessageData
import com.domain.model.FriendItemData
import com.domain.model.GeoCodeData
import com.domain.model.ReverseGeoCodeData
import com.domain.model.SearchData
import com.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getUserInfo(): UserInfo?

    suspend fun upsertUserInfo(userInfo: UserInfo)

    fun getUserInfoFlow(): Flow<UserInfo>

    fun getAddressListFlow(): Flow<List<AddressItemData>>

    suspend fun getAddressList(): List<AddressItemData>

    suspend fun upsertAddressList(addressData: AddressItemData)

    suspend fun deleteAddress(address: String)

    suspend fun deleteAllAddress()

    suspend fun checkAddressData(addressData: AddressItemData): Int

    suspend fun updateAddressDataList(addressDataList: List<AddressItemData>)

    fun getFriendListFlow(): Flow<List<FriendItemData>>

    suspend fun getFriendList(): List<FriendItemData>

    suspend fun upsertFriendData(email: String, friendItemData: FriendItemData)

    suspend fun deleteAllFriendData()

    suspend fun deleteFriendData(email: String)

    suspend fun initChatData(data: ChatData)

    suspend fun updateChatData(data: ChatMessageData, code: String)

    fun getChatList(code: String): Flow<ChatData?>

    suspend fun getChat(code: String): ChatData?

    fun getAllChatData(): Flow<List<ChatData>>

    suspend fun deleteChat()

    suspend fun getGeoCode(address: String): GeoCodeData

    suspend fun getReverseGeoCode(coords: String): ReverseGeoCodeData

    suspend fun getKeywordSearch(keyword: String): SearchData
}